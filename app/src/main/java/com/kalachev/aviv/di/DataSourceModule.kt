package com.kalachev.aviv.di

import com.kalachev.aviv.layer.data.local.LocalDataSource
import org.koin.dsl.module

val dataSourceModule = module {
    single<LocalDataSource> {
        LocalDataSource(
            appDatabase = get(),
            flatDao = get(),
            flatDetailsDao = get()
        )
    }
}