package com.kalachev.aviv.layer.presentation.details.model

sealed interface DetailsEvent {
    data class ScreenOpened(val id: Long) : DetailsEvent
    data object Refresh : DetailsEvent
}