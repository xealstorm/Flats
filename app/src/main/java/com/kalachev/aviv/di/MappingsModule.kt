package com.kalachev.aviv.di

import com.kalachev.aviv.layer.domain.model.mappings.FlatDetailsMappings
import com.kalachev.aviv.layer.domain.model.mappings.FlatMappings
import org.koin.dsl.module

val mappingsModule = module {
    single<FlatDetailsMappings> { FlatDetailsMappings() }
    single<FlatMappings> { FlatMappings() }
}