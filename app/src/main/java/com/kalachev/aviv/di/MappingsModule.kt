package com.kalachev.aviv.di

import com.kalachev.aviv.layer.domain.model.mappings.FlatDetailsMappings
import com.kalachev.aviv.layer.domain.model.mappings.FlatMappings
import com.kalachev.aviv.layer.presentation.details.model.mappings.DetailsMappings
import com.kalachev.aviv.layer.presentation.feed.model.mappings.FeedItemMappings
import org.koin.dsl.module

val mappingsModule = module {
    single<FlatDetailsMappings> { FlatDetailsMappings() }

    single<FlatMappings> { FlatMappings() }

    single<FeedItemMappings> { FeedItemMappings() }

    single<DetailsMappings> { DetailsMappings() }
}