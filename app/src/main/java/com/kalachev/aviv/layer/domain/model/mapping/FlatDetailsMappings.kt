package com.kalachev.aviv.layer.domain.model.mapping

import com.kalachev.aviv.layer.data.local.model.LocalFlatDetails
import com.kalachev.aviv.layer.data.remote.model.RemoteFlatDetails
import com.kalachev.aviv.layer.domain.model.FlatDetails

class FlatDetailsMappings {
    fun toData(): (FlatDetails) -> LocalFlatDetails = {
        LocalFlatDetails(
            localId = 0,
            id = it.id,
            city = it.city,
            area = it.area,
            imageUrl = it.imageUrl,
            price = it.price,
            professional = it.professional,
            offerType = it.offerType,
            propertyType = it.propertyType
        )
    }


    fun localToDomain(): (LocalFlatDetails) -> FlatDetails = {
        FlatDetails(
            id = it.id,
            city = it.city,
            area = it.area,
            imageUrl = it.imageUrl,
            price = it.price,
            professional = it.professional,
            offerType = it.offerType,
            propertyType = it.propertyType
        )
    }

    fun remoteToDomain(): (RemoteFlatDetails) -> FlatDetails? = {
        if (it.id != null &&
            it.city != null &&
            it.area != null &&
            it.offerType != null
        ) {
            FlatDetails(
                id = it.id,
                city = it.city,
                area = it.area,
                imageUrl = it.url,
                price = it.price,
                professional = it.professional,
                offerType = it.offerType,
                propertyType = it.propertyType
            )
        } else {
            null
        }
    }
}