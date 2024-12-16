package com.kalachev.aviv.layer.presentation.details.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailsModel(
    val id: Long,
    val bedrooms: Int,
    val rooms: Int,
    val city: String,
    val area: Double,
    val imageUrl: String?,
    val price: String,
    val professional: String,
    val offerType: Int,
    val propertyType: String,
): Parcelable