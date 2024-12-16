package com.kalachev.aviv.layer.domain.model

import java.math.BigDecimal

data class FlatDetails(
    val id: Long,
    val bedrooms: Int,
    val rooms: Int,
    val city: String,
    val area: Double,
    val imageUrl: String?,
    val price: BigDecimal?,
    val professional: String?,
    val offerType: Int,
    val propertyType: String?,
)