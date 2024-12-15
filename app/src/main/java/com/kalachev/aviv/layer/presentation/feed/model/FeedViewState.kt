package com.kalachev.aviv.layer.presentation.feed.model

data class FeedViewState(
    val items: List<FeedItemModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: Throwable? = null
)