package com.kalachev.aviv.layer.presentation.details.model

interface DetailsEvent {
    data class ScreenOpened(val id: Long) : DetailsEvent
}