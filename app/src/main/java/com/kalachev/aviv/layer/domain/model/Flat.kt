package com.kalachev.aviv.layer.domain.model

import java.math.BigDecimal

data class Flat(
    val id: Long,
    val city: String,
    val area: Double,
    val imageUrl: String?,
    val price: BigDecimal?,
    val professional: String?,
    val offerType: Int,
    val propertyType: String?,
)