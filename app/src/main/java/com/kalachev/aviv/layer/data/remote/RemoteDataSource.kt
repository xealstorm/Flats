package com.kalachev.aviv.layer.data.remote

import android.annotation.SuppressLint
import com.kalachev.aviv.layer.data.remote.model.RemoteFlatDetails
import com.kalachev.aviv.layer.data.remote.model.RemoteFlatsResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

class RemoteDataSource(private val client: HttpClient) {

    /**
     * Performs a network call to request a list of all the flats.
     */
    suspend fun getAllFlats(): Result<RemoteFlatsResponse?> =
        try {
            val response: HttpResponse = client.get(GET_FLATS_URL)
            if (response.status == HttpStatusCode.OK) {
                Result.success(response.body<RemoteFlatsResponse?>())
            } else {
                Result.failure(IllegalArgumentException(DEFAULT_ERROR_TEXT))
            }
        } catch (e: ClientRequestException) {
            Result.failure(e)
        } catch (e: ServerResponseException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }

    /**
     * Performs a network call to request details of a flat of a particular id.
     */
    @SuppressLint("DefaultLocale")
    suspend fun getFlatDetailsById(id: Long): Result<RemoteFlatDetails?> =
        try {
            val response: HttpResponse = client.get(String.format(GET_FLAT_DETAILS_URL, id))
            if (response.status == HttpStatusCode.OK) {
                Result.success(response.body<RemoteFlatDetails?>())
            } else {
                Result.failure(IllegalArgumentException(DEFAULT_ERROR_TEXT))
            }
        } catch (e: ClientRequestException) {
            Result.failure(e)
        } catch (e: ServerResponseException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }

    companion object {
        const val GET_FLATS_URL = "/listings.json"
        const val GET_FLAT_DETAILS_URL = "/listings/%d.json"
        const val DEFAULT_ERROR_TEXT = "Error getting remote data"
    }
}