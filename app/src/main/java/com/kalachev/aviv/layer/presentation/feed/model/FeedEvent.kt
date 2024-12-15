package com.kalachev.aviv.layer.presentation.feed.model

sealed interface FeedEvent {
    data object ScreenOpened : FeedEvent
    data object PullToRefresh : FeedEvent
    data class ItemTapped(val clientId: Long) : FeedEvent
}