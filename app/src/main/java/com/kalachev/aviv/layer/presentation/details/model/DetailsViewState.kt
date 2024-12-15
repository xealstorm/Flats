package com.kalachev.aviv.layer.presentation.details.model

data class DetailsViewState(
    val details: DetailsModel? = null,
    val isLoading: Boolean = false,
    val error: Throwable? = null
)