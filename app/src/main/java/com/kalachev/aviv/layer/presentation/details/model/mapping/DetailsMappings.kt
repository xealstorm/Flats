package com.kalachev.aviv.layer.presentation.details.model.mapping

import com.kalachev.aviv.ext.toGoodLookingString
import com.kalachev.aviv.layer.domain.model.FlatDetails
import com.kalachev.aviv.layer.presentation.details.model.DetailsModel

class DetailsMappings {
    fun toPresentation(): (FlatDetails) -> DetailsModel = {
        DetailsModel(
            id = it.id,
            bedrooms = it.bedrooms,
            rooms = it.rooms,
            city = it.city,
            area = it.area.toBigDecimal().toGoodLookingString(),
            imageUrl = it.imageUrl,
            price = if (it.price != null) {
                it.price.toGoodLookingString()
            } else {
                ""
            },
            professional = it.professional ?: "",
            offerType = it.offerType,
            propertyType = it.propertyType ?: "",
        )
    }
}