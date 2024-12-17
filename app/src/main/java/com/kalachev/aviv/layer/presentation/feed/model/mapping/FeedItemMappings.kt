package com.kalachev.aviv.layer.presentation.feed.model.mapping

import com.kalachev.aviv.ext.toGoodLookingString
import com.kalachev.aviv.layer.domain.model.Flat
import com.kalachev.aviv.layer.presentation.feed.model.FeedItemModel

class FeedItemMappings {
    fun toPresentation(): (Flat) -> FeedItemModel = {
        FeedItemModel(
            id = it.id,
            city = it.city,
            thumbUrl = it.imageUrl,
            // The price is formatted as a string with a trailing zero removed if it is a whole number
            // and as a string with two decimal places otherwise.
            // In an edge-case scenario, if the price is null, it is represented as "N/A" (hardcoded string is used for simplicity).
            price = if (it.price != null) {
                it.price.toGoodLookingString()
            } else {
                ""
            },
        )
    }
}