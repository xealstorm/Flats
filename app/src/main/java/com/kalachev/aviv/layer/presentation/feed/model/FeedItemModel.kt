package com.kalachev.aviv.layer.presentation.feed.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FeedItemModel(
    val id: Long,
    val thumbUrl: String?,
    val price: String,
    val city: String,
) : Parcelable