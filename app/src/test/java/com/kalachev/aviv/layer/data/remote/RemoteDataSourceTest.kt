package com.kalachev.aviv.layer.data.remote

import com.kalachev.aviv.layer.data.remote.model.RemoteFlat
import com.kalachev.aviv.layer.data.remote.model.RemoteFlatDetails
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal

class RemoteDataSourceTest {

    private lateinit var mockEngine: MockEngine
    private lateinit var client: HttpClient
    private lateinit var remoteDataSource: RemoteDataSource

    @Before
    fun setUp() {
        mockEngine = MockEngine { request ->
            when (request.url.encodedPath) {
                "/listings.json" -> respond(
                    content = """
                        {
                            "items": [
                                {"id": 1, "bedrooms": 2, "rooms": 3, "city": "City 1", "area": 100.0, "url": "http://example.com", "price": 1000.0, "professional": "Professional 1", "offerType": 1, "propertyType": "Type 1"}
                            ],
                            "totalCount": 1
                        }
                    """.trimIndent(),
                    headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                )

                "/listings/1.json" -> respond(
                    content = """{"id": 1, "bedrooms": 2, "rooms": 3, "city": "City 1", "area": 100.0, "url": "http://example.com", "price": 1000.0, "professional": "Professional 1", "offerType": 1, "propertyType": "Type 1"}""",
                    headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                )

                else -> respondError(HttpStatusCode.NotFound)
            }
        }

        client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
        }

        remoteDataSource = RemoteDataSource(client)
    }

    @Test
    fun `getAllFlats provides a successful result`() = runTest {
        val result = remoteDataSource.getAllFlats()
        assertTrue(result.isSuccess)
        assertEquals(
            listOf(
                RemoteFlat(
                    id = 1,
                    bedrooms = 2,
                    rooms = 3,
                    city = "City 1",
                    area = 100.0,
                    url = "http://example.com",
                    price = BigDecimal("1000.0"),
                    professional = "Professional 1",
                    offerType = 1,
                    propertyType = "Type 1"
                )
            ),
            result.getOrNull()?.items
        )
    }

    @Test
    fun `getFlatDetailsById provides a successful result`() = runTest {
        val result = remoteDataSource.getFlatDetailsById(1)
        assertTrue(result.isSuccess)
        assertEquals(
            RemoteFlatDetails(
                id = 1,
                bedrooms = 2,
                rooms = 3,
                city = "City 1",
                area = 100.0,
                url = "http://example.com",
                price = BigDecimal("1000.0"),
                professional = "Professional 1",
                offerType = 1,
                propertyType = "Type 1"
            ), result.getOrNull()
        )
    }

    @Test
    fun `getAllFlats provides a failure upon error`() = runTest {
        mockEngine = MockEngine { respondError(HttpStatusCode.InternalServerError) }
        client = HttpClient(mockEngine)
        remoteDataSource = RemoteDataSource(client)

        val result = remoteDataSource.getAllFlats()
        assertTrue(result.isFailure)
    }

    @Test
    fun `getFlatDetailsById provides a failure upon error`() = runTest {
        mockEngine = MockEngine { respondError(HttpStatusCode.InternalServerError) }
        client = HttpClient(mockEngine)
        remoteDataSource = RemoteDataSource(client)

        val result = remoteDataSource.getFlatDetailsById(1)
        assertTrue(result.isFailure)
    }
}