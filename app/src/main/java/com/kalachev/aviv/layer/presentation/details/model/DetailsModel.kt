package com.kalachev.aviv.layer.presentation.details.model

data class DetailsModel(
    val id: Long,
    val city: String,
    val area: Double,
    val imageUrl: String?,
    val price: String,
    val professional: String,
    val offerType: Int,
    val propertyType: String,
)