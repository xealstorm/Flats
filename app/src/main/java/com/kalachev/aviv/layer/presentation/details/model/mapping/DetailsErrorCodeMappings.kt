package com.kalachev.aviv.layer.presentation.details.model.mapping

import android.os.RemoteException
import com.kalachev.aviv.layer.presentation.details.model.DetailsErrorCode
import com.kalachev.aviv.layer.presentation.feed.model.FeedErrorCode
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException

class DetailsErrorCodeMappings {
    fun toPresentation(): (Throwable) -> DetailsErrorCode = {
        when(it) {
            is RemoteException,
            is ServerResponseException -> DetailsErrorCode.REMOTE_EXCEPTION
            is ClientRequestException,
            is IllegalArgumentException -> DetailsErrorCode.LOCAL_EXCEPTION
            else -> DetailsErrorCode.UNKNOWN_EXCEPTION
        }
    }
}
