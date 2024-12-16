package com.kalachev.aviv.layer.presentation.feed

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalachev.aviv.layer.domain.usecase.GetAllFlats
import com.kalachev.aviv.layer.domain.usecase.SyncFlats
import com.kalachev.aviv.layer.presentation.feed.model.FeedEvent
import com.kalachev.aviv.layer.presentation.feed.model.FeedItemModel
import com.kalachev.aviv.layer.presentation.feed.model.FeedUiAction
import com.kalachev.aviv.layer.presentation.feed.model.FeedViewState
import com.kalachev.aviv.layer.presentation.feed.model.mapping.FeedErrorCodeMappings
import com.kalachev.aviv.layer.presentation.feed.model.mapping.FeedItemMappings
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FeedViewModel(
    private val getAllFlats: GetAllFlats,
    private val syncFlats: SyncFlats,
    private val savedStateHandle: SavedStateHandle,
    private val feedItemMappings: FeedItemMappings,
    private val feedErrorCodeMappings: FeedErrorCodeMappings,
) : ViewModel() {

    private val _feedViewState: MutableStateFlow<FeedViewState> = MutableStateFlow(
        savedStateHandle.get<FeedViewState>(SAVED_STATE_HANDLE_KEY_FEED) ?: FeedViewState()
    )
    val feedViewState = _feedViewState.asStateFlow()

    private val _feedUiAction = MutableSharedFlow<FeedUiAction>()
    val feedUiAction = _feedUiAction.asSharedFlow()

    fun handleEvent(event: FeedEvent) {
        when (event) {
            FeedEvent.ScreenOpened -> {
                observeItems()
                refreshFeed()
            }

            FeedEvent.PullToRefresh -> {
                refreshFeed()
            }

            is FeedEvent.ItemTapped -> {
                openDetails(event.clientId)
            }
        }
    }

    private fun refreshFeed() = viewModelScope.launch {
        updateStateAsLoading()
        syncFlats()
            .onSuccess {
                updateStateAsNoLongerLoading()
            }
            .onFailure { error ->
                updateStateAsError(error)
            }
    }

    private fun observeItems() = viewModelScope.launch {
        getAllFlats()
            .catch { error ->
                updateStateAsError(error)
            }
            .collect { result ->
                result.onSuccess { domainItems ->
                    updateStateAsSuccess(
                        domainItems.map { domainItem ->
                            domainItem.let(feedItemMappings.toPresentation())
                        }
                    )
                }.onFailure { error ->
                    updateStateAsError(error)
                }
            }
    }

    private fun openDetails(id: Long) = viewModelScope.launch {
        _feedUiAction.emit(FeedUiAction.OpenDetails(id))
    }

    private fun updateStateAsLoading() {
        _feedViewState.update {
            it.copy(
                isLoading = true,
                error = null
            )
        }
    }

    private fun updateStateAsNoLongerLoading() {
        _feedViewState.update {
            it.copy(
                isLoading = false,
                error = null
            )
        }
    }

    private fun updateStateAsError(error: Throwable) {
        _feedViewState.update {
            it.copy(
                isLoading = false,
                error = error.let(feedErrorCodeMappings.toPresentation())
            )
        }
    }

    private fun updateStateAsSuccess(feedItems: List<FeedItemModel>) {
        _feedViewState.update {
            it.copy(
                items = feedItems,
                isLoading = false,
                error = null
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        savedStateHandle[SAVED_STATE_HANDLE_KEY_FEED] = _feedViewState.value
    }

    companion object {
        const val SAVED_STATE_HANDLE_KEY_FEED = "saved_state_handle_key_feed"
    }

}