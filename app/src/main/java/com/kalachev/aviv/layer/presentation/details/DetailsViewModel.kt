package com.kalachev.aviv.layer.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalachev.aviv.layer.domain.usecase.GetFlatDetails
import com.kalachev.aviv.layer.domain.usecase.SyncFlatDetails
import com.kalachev.aviv.layer.presentation.details.model.DetailsEvent
import com.kalachev.aviv.layer.presentation.details.model.DetailsModel
import com.kalachev.aviv.layer.presentation.details.model.DetailsViewState
import com.kalachev.aviv.layer.presentation.details.model.mapping.DetailsErrorCodeMappings
import com.kalachev.aviv.layer.presentation.details.model.mapping.DetailsMappings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val getFlatDetails: GetFlatDetails,
    private val syncFlatDetails: SyncFlatDetails,
    private val savedStateHandle: SavedStateHandle,
    private val detailsMappings: DetailsMappings,
    private val detailsErrorCodeMappings: DetailsErrorCodeMappings,
) : ViewModel() {

    private val _detailsState = MutableStateFlow(
        savedStateHandle.get<DetailsViewState>(SAVED_STATE_HANDLE_KEY_DETAILS) ?: DetailsViewState()
    )
    val detailsState: StateFlow<DetailsViewState> = _detailsState.asStateFlow()

    fun handleEvent(event: DetailsEvent) {
        when (event) {
            is DetailsEvent.ScreenOpened -> {
                initStateWithId(event.id)
                _detailsState.value.id?.let {
                    observeDetails(it)
                    refreshDetails(it)
                }
            }

            is DetailsEvent.Refresh -> _detailsState.value.id?.let { refreshDetails(it) }
        }
    }

    private fun observeDetails(id: Long) = viewModelScope.launch {
        getFlatDetails(id)
            .catch { error ->
                updateStateAsError(error)
            }
            .collect { result ->
                result.onSuccess { detailsDomain ->
                    detailsDomain?.let { updateStateAsSuccess(it.let(detailsMappings.toPresentation())) }
                }.onFailure { error ->
                    updateStateAsError(error)
                }
            }
    }

    private fun refreshDetails(id: Long) = viewModelScope.launch {
        updateStateAsLoading()
        syncFlatDetails(id)
            .onSuccess {
                updateStateAsNoLongerLoading()
            }.onFailure { error ->
                updateStateAsError(error)
            }
    }

    private fun updateStateAsLoading() {
        _detailsState.update {
            it.copy(
                isLoading = true,
                error = null
            )
        }
    }

    private fun updateStateAsNoLongerLoading() {
        _detailsState.update {
            it.copy(
                isLoading = false,
                error = null
            )
        }
    }

    private fun updateStateAsError(error: Throwable) {
        _detailsState.update {
            it.copy(
                isLoading = false,
                error = error.let(detailsErrorCodeMappings.toPresentation())
            )
        }
    }

    private fun updateStateAsSuccess(details: DetailsModel) {
        _detailsState.update {
            it.copy(
                details = details,
                isLoading = false,
                error = null
            )
        }
    }

    private fun initStateWithId(id: Long) {
        _detailsState.update { it.copy(id = id) }
    }

    override fun onCleared() {
        super.onCleared()
        savedStateHandle[SAVED_STATE_HANDLE_KEY_DETAILS] = _detailsState.value
    }

    companion object {
        const val SAVED_STATE_HANDLE_KEY_DETAILS = "saved_state_handle_key_details"
    }

}