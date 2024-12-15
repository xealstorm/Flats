package com.kalachev.aviv.layer.presentation.feed.model

data class FeedItemModel(
    val id: Long,
    val thumbUrl: String?,
    val price: String,
    val city: String,
)