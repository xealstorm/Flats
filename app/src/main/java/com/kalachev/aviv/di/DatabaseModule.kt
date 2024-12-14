package com.kalachev.aviv.di

import androidx.room.Room
import com.kalachev.aviv.layer.data.local.AppDatabase
import com.kalachev.aviv.layer.data.local.dao.FlatDao
import com.kalachev.aviv.layer.data.local.dao.FlatDetailsDao
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            name = "aviv_db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    single<FlatDao> {
        get<AppDatabase>().flatDao()
    }

    single<FlatDetailsDao> {
        get<AppDatabase>().flatDetailsDao()
    }
}