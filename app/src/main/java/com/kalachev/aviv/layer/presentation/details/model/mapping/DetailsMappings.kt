package com.kalachev.aviv.layer.presentation.details.model.mapping

import com.kalachev.aviv.layer.domain.model.FlatDetails
import com.kalachev.aviv.layer.presentation.details.model.DetailsModel

class DetailsMappings {
    fun toPresentation(): (FlatDetails) -> DetailsModel = {
        DetailsModel(
            id = it.id,
            city = it.city,
            area = it.area,
            imageUrl = it.imageUrl,
            price = it.price.toString(),
            professional = it.professional ?: "",
            offerType = it.offerType,
            propertyType = it.propertyType ?: "",
        )
    }
}