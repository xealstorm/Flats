package com.kalachev.aviv.layer.presentation.feed.model.mapping

import com.kalachev.aviv.layer.domain.model.Flat
import com.kalachev.aviv.layer.presentation.feed.model.FeedItemModel

class FeedItemMappings {
    fun toPresentation(): (Flat) -> FeedItemModel = {
        FeedItemModel(
            id = it.id,
            city = it.city,
            thumbUrl = it.imageUrl,
            price = it.price.toString(),
        )
    }
}