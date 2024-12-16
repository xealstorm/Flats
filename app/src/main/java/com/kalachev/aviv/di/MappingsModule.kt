package com.kalachev.aviv.di

import com.kalachev.aviv.layer.domain.model.mapping.FlatDetailsMappings
import com.kalachev.aviv.layer.domain.model.mapping.FlatMappings
import com.kalachev.aviv.layer.presentation.details.model.mapping.DetailsErrorCodeMappings
import com.kalachev.aviv.layer.presentation.details.model.mapping.DetailsMappings
import com.kalachev.aviv.layer.presentation.feed.model.mapping.FeedErrorCodeMappings
import com.kalachev.aviv.layer.presentation.feed.model.mapping.FeedItemMappings
import org.koin.dsl.module

val mappingsModule = module {
    single<FlatDetailsMappings> { FlatDetailsMappings() }

    single<FlatMappings> { FlatMappings() }

    single<FeedItemMappings> { FeedItemMappings() }

    single<DetailsMappings> { DetailsMappings() }

    single<FeedErrorCodeMappings> { FeedErrorCodeMappings() }

    single<DetailsErrorCodeMappings> { DetailsErrorCodeMappings() }
}