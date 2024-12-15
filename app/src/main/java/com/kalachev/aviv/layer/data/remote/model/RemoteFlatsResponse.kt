package com.kalachev.aviv.layer.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class RemoteFlatsResponse(
    val items: List<RemoteFlat?>?,
    val totalCount: Int?
)