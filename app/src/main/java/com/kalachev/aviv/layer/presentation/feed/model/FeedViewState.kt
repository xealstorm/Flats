package com.kalachev.aviv.layer.presentation.feed.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FeedViewState(
    val items: List<FeedItemModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: Throwable? = null
): Parcelable