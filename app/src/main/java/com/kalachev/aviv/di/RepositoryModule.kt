package com.kalachev.aviv.di

import com.kalachev.aviv.layer.data.repo_impl.FlatDetailsRepositoryImpl
import com.kalachev.aviv.layer.data.repo_impl.FlatRepositoryImpl
import com.kalachev.aviv.layer.domain.repo.FlatDetailsRepository
import com.kalachev.aviv.layer.domain.repo.FlatRepository
import org.koin.dsl.module

val repositoryModule = module {
    factory<FlatRepository> {
        FlatRepositoryImpl(
            remoteDataSource = get(),
            localDataSource = get(),
            flatMappings = get()
        )
    }

    factory<FlatDetailsRepository> {
        FlatDetailsRepositoryImpl(
            remoteDataSource = get(),
            localDataSource = get(),
            flatDetailsMappings = get()
        )
    }
}
