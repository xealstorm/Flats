package com.kalachev.aviv.layer.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "flats")
data class LocalFlat(
    @PrimaryKey(autoGenerate = true)
    val localId: Long = 0,
    val id: Long,
    val city: String,
    val area: Double,
    val imageUrl: String?,
    val price: BigDecimal?,
    val professional: String?,
    val offerType: Int,
    val propertyType: String?,
)