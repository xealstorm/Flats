package com.kalachev.aviv.di

import com.kalachev.aviv.layer.domain.usecase.GetAllFlats
import com.kalachev.aviv.layer.domain.usecase.GetFlatDetails
import com.kalachev.aviv.layer.domain.usecase.SyncFlatDetails
import com.kalachev.aviv.layer.domain.usecase.SyncFlats
import org.koin.dsl.module

val useCaseModule = module {
    factory<GetAllFlats> {
        GetAllFlats(
            flatRepository = get()
        )
    }

    factory<GetFlatDetails> {
        GetFlatDetails(
            flatDetailsRepository = get()
        )
    }

    factory<SyncFlats> {
        SyncFlats(
            flatRepository = get()
        )
    }

    factory<SyncFlatDetails> {
        SyncFlatDetails(
            flatDetailsRepository = get()
        )
    }
}
