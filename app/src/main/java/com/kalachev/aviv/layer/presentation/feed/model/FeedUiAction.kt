package com.kalachev.aviv.layer.presentation.feed.model

sealed interface FeedUiAction {
    data class OpenDetails(val id: Long?) : FeedUiAction
}