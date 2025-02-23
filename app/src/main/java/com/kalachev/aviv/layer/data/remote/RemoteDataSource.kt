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
import kotlinx.coroutines.CancellationException

class RemoteDataSource(private val client: HttpClient) {

    /**
     * Performs a network call to request a list of all the flats.
     */
    suspend fun getAllFlats(): Result<RemoteFlatsResponse?> =
        handleApiCall {
            client.get(GET_FLATS_URL).body()
        }

    /**
     * Performs a network call to request details of a flat of a particular id.
     */
    @SuppressLint("DefaultLocale")
    suspend fun getFlatDetailsById(id: Long): Result<RemoteFlatDetails?> =
        handleApiCall {
            client.get(String.format(GET_FLAT_DETAILS_URL, id)).body()
        }

    /**
     * A generic function to perform a network call and handle exceptions.
     */
    private suspend inline fun <T : HttpResponse, reified R> handleApiCall(crossinline apiCall: suspend () -> T): Result<R> =
        try {
            val response: T = apiCall()
            if (response.status == HttpStatusCode.OK) {
                Result.success(response.body<R>())
            } else {
                Result.failure(IllegalArgumentException(DEFAULT_ERROR_TEXT))
            }
        } catch (e: ClientRequestException) {
            Result.failure(e)
        } catch (e: ServerResponseException) {
            Result.failure(e)
        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
            }
            Result.failure(e)
        }


    companion object {
        const val GET_FLATS_URL = "/listings.json"
        const val GET_FLAT_DETAILS_URL = "/listings/%d.json"
        const val DEFAULT_ERROR_TEXT = "Error getting remote data"
    }
}