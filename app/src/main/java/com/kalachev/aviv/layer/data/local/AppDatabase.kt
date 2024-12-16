package com.kalachev.aviv.layer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kalachev.aviv.layer.data.local.dao.FlatDao
import com.kalachev.aviv.layer.data.local.dao.FlatDetailsDao
import com.kalachev.aviv.layer.data.local.model.LocalFlat
import com.kalachev.aviv.layer.data.local.model.LocalFlatDetails
import com.kalachev.aviv.layer.data.local.model.converters.BigDecimalConverter

@Database(entities = [LocalFlat::class, LocalFlatDetails::class], version = 3)
@TypeConverters(BigDecimalConverter::class)
abstract class AppDatabase : RoomDatabase(){
    abstract fun flatDao(): FlatDao
    abstract fun flatDetailsDao(): FlatDetailsDao
}