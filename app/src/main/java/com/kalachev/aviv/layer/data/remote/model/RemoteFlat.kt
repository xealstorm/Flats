package com.kalachev.aviv.layer.data.remote.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class RemoteFlat(
    val id: Long?,
    val city: String?,
    val area: Double?,
    val url: String?,
    @Contextual
    val price: BigDecimal?,
    val professional: String?,
    val offerType: Int?,
    val propertyType: String?,
)