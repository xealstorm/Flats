package com.kalachev.aviv.layer.presentation.feed.model.mapping

import android.os.RemoteException
import com.kalachev.aviv.layer.presentation.feed.model.FeedErrorCode
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException

class FeedErrorCodeMappings {
    fun toPresentation(): (Throwable) -> FeedErrorCode = {
        when(it) {
            is RemoteException,
            is ServerResponseException -> FeedErrorCode.REMOTE_EXCEPTION
            is ClientRequestException,
            is IllegalArgumentException -> FeedErrorCode.LOCAL_EXCEPTION
            else -> FeedErrorCode.UNKNOWN_EXCEPTION
        }
    }
}
