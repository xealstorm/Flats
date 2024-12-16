package com.kalachev.aviv.layer.data.local.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(
    tableName = "flatdetails",
    indices = [Index(value = ["id"], unique = true)]
)
data class LocalFlatDetails(
    @PrimaryKey(autoGenerate = true)
    val localId: Long = 0,
    val id: Long,
    val bedrooms: Int = 0,
    val rooms: Int = 0,
    val city: String,
    val area: Double,
    val imageUrl: String?,
    val price: BigDecimal?,
    val professional: String?,
    val offerType: Int,
    val propertyType: String?,
)