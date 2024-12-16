package com.kalachev.aviv.layer.presentation.details.model

enum class DetailsErrorCode {
    REMOTE_EXCEPTION, // Server error in case of a RemoteException, ServerResponseException
    LOCAL_EXCEPTION, // Client error in case of a ClientRequestException, IllegalArgumentException
    UNKNOWN_EXCEPTION // Unknown error in case of any other exception
}