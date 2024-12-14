package com.kalachev.aviv.di

import com.kalachev.aviv.layer.data.local.LocalDataSource
import com.kalachev.aviv.layer.data.remote.RemoteDataSource
import org.koin.dsl.module

val dataSourceModule = module {
    single<LocalDataSource> {
        LocalDataSource(
            appDatabase = get(),
            flatDao = get(),
            flatDetailsDao = get()
        )
    }

    single<RemoteDataSource> {
        RemoteDataSource(
            client = get()
        )
    }
}