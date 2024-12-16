package com.kalachev.aviv.layer.data.remote.model

import com.kalachev.aviv.layer.data.remote.model.serializer.BigDecimalSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class RemoteFlat(
    val id: Long? = null,
    val bedrooms: Int? = null,
    val rooms: Int? = null,
    val city: String? = null,
    val area: Double? = null,
    val url: String? = null,
    @Serializable(with = BigDecimalSerializer::class)
    val price: BigDecimal? = null,
    val professional: String? = null,
    val offerType: Int? = null,
    val propertyType: String? = null,
)