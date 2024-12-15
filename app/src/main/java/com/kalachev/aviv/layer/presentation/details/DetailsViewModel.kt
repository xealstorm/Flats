package com.kalachev.aviv.layer.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalachev.aviv.layer.domain.usecase.GetFlatDetails
import com.kalachev.aviv.layer.domain.usecase.SyncFlatDetails
import com.kalachev.aviv.layer.presentation.details.model.DetailsEvent
import com.kalachev.aviv.layer.presentation.details.model.DetailsModel
import com.kalachev.aviv.layer.presentation.details.model.DetailsViewState
import com.kalachev.aviv.layer.presentation.details.model.mappings.DetailsMappings
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
) : ViewModel() {

    private val _detailsState = MutableStateFlow(
        savedStateHandle.get<DetailsViewState>(SAVED_STATE_HANDLE_KEY_DETAILS) ?: DetailsViewState()
    )
    val detailsState: StateFlow<DetailsViewState> = _detailsState.asStateFlow()

    fun handleEvent(event: DetailsEvent) {
        when (event) {
            is DetailsEvent.ScreenOpened -> {
                observeDetails(event.id)
                refreshDetails(event.id)
            }
        }
    }

    private fun observeDetails(id: Long) = viewModelScope.launch {
        getFlatDetails(id)
            .catch { error ->
                updateStateAsError(error)
            }
            .collect { result ->
                result.onSuccess { detailsDomain ->
                    updateStateAsSuccess(detailsDomain.let(detailsMappings.toPresentation()))
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
                error = error
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

    override fun onCleared() {
        super.onCleared()
        savedStateHandle[SAVED_STATE_HANDLE_KEY_DETAILS] = _detailsState.value
    }

    companion object {
        const val SAVED_STATE_HANDLE_KEY_DETAILS = "saved_state_handle_key_details"
    }

}