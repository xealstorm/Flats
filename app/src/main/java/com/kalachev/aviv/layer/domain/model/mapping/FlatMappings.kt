package com.kalachev.aviv.layer.domain.model.mapping

import com.kalachev.aviv.layer.data.local.model.LocalFlat
import com.kalachev.aviv.layer.data.remote.model.RemoteFlat
import com.kalachev.aviv.layer.domain.model.Flat

class FlatMappings {
    fun toData(): (Flat) -> LocalFlat = {
        LocalFlat(
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


    fun localToDomain(): (LocalFlat) -> Flat = {
        Flat(
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

    fun remoteToDomain(): (RemoteFlat) -> Flat? = {
        if (it.id != null &&
            it.city != null &&
            it.area != null &&
            it.offerType != null
        ) {
            Flat(
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