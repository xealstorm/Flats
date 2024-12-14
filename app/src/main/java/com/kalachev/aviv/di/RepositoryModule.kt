package com.kalachev.aviv.di

import com.kalachev.aviv.layer.data.repo_impl.FlatDetailsRepositoryImpl
import com.kalachev.aviv.layer.data.repo_impl.FlatRepositoryImpl
import com.kalachev.aviv.layer.domain.repo.FlatDetailsRepository
import com.kalachev.aviv.layer.domain.repo.FlatRepository
import org.koin.dsl.module

val repositoryModule = module {
    factory<FlatRepository> {
        FlatRepositoryImpl(
            get(),
            get(),
            get()
        )
    }

    factory<FlatDetailsRepository> {
        FlatDetailsRepositoryImpl(
            get(),
            get(),
            get()
        )
    }
}
