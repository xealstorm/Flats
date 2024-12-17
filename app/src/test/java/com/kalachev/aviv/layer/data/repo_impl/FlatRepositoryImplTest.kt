package com.kalachev.aviv.layer.data.repo_impl

import com.kalachev.aviv.layer.data.local.LocalDataSource
import com.kalachev.aviv.layer.data.local.model.LocalFlat
import com.kalachev.aviv.layer.data.remote.RemoteDataSource
import com.kalachev.aviv.layer.data.remote.model.RemoteFlat
import com.kalachev.aviv.layer.data.remote.model.RemoteFlatsResponse
import com.kalachev.aviv.layer.domain.model.Flat
import com.kalachev.aviv.layer.domain.model.mapping.FlatMappings
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyList
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import java.math.BigDecimal

class FlatRepositoryImplTest {

    private val remoteDataSource: RemoteDataSource = mock()
    private val localDataSource: LocalDataSource = mock()
    private val flatMappings: FlatMappings = mock()
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var flatRepository: FlatRepositoryImpl

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        flatRepository = FlatRepositoryImpl(remoteDataSource, localDataSource, flatMappings, testDispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `syncFlats should return success when remote data is valid`() = runTest {
        val remoteFlatsResponse = RemoteFlatsResponse(
            listOf(
                RemoteFlat(
                    id = 1,
                    bedrooms = 2,
                    rooms = 3,
                    city = "City 1",
                    area = 100.0,
                    url = "url1",
                    price = BigDecimal(100000),
                    professional = "Professional 1",
                    offerType = 1,
                    propertyType = "Type 1"
                ),
                RemoteFlat(
                    id = 2,
                    bedrooms = 3,
                    rooms = 4,
                    city = "City 2",
                    area = 150.0,
                    url = "url2",
                    price = BigDecimal(150000),
                    professional = "Professional 2",
                    offerType = 2,
                    propertyType = "Type 2"
                )
            ), 2
        )
        `when`(remoteDataSource.getAllFlats()).thenReturn(Result.success(remoteFlatsResponse))
        `when`(localDataSource.getAllFlats()).thenReturn(emptyList())
        `when`(flatMappings.remoteToDomain()).thenReturn { remoteFlat: RemoteFlat ->
            Flat(
                id = remoteFlat.id!!,
                bedrooms = remoteFlat.bedrooms!!,
                rooms = remoteFlat.rooms!!,
                city = remoteFlat.city!!,
                area = remoteFlat.area!!,
                imageUrl = remoteFlat.url!!,
                price = remoteFlat.price,
                professional = remoteFlat.professional,
                offerType = remoteFlat.offerType!!,
                propertyType = remoteFlat.propertyType
            )
        }
        `when`(flatMappings.toData()).thenReturn { flat: Flat ->
            LocalFlat(
                id = flat.id,
                bedrooms = flat.bedrooms,
                rooms = flat.rooms,
                city = flat.city,
                area = flat.area,
                imageUrl = flat.imageUrl,
                price = flat.price,
                professional = flat.professional,
                offerType = flat.offerType,
                propertyType = flat.propertyType
            )
        }

        val result = flatRepository.syncFlats()

        assertTrue(result.isSuccess)
        verify(localDataSource).updateFlatsAndFlatDetails(
            anyList(), anyList(), anyList()
        )
    }

    @Test
    fun `syncFlats should return failure when remote data is invalid`() = runTest {
        `when`(remoteDataSource.getAllFlats()).thenReturn(Result.failure(Exception("Network error")))
        `when`(localDataSource.getAllFlats()).thenReturn(emptyList())
        val result = flatRepository.syncFlats()

        assertTrue(result.isFailure)
        assertEquals("Network error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `getAllFlatsAsFlow should return mapped flats`() = runTest {
        val localFlats = listOf(
            LocalFlat(
                localId = 1,
                id = 2,
                bedrooms = 3,
                rooms = 3,
                city = "City 1",
                area = 100.0,
                imageUrl = "url1",
                price = BigDecimal(100000),
                professional = "Professional 1",
                offerType = 1,
                propertyType = "Type 1"
            ),
            LocalFlat(
                localId = 2,
                id = 3,
                bedrooms = 4,
                rooms = 4,
                city = "City 2",
                area = 150.0,
                imageUrl = "url2",
                price = BigDecimal(150000),
                professional = "Professional 2",
                offerType = 2,
                propertyType = "Type 2"
            )
        )
        val flats = listOf(
            Flat(
                id = 2,
                bedrooms = 3,
                rooms = 3,
                city = "City 1",
                area = 100.0,
                imageUrl = "url1",
                price = BigDecimal(100000),
                professional = "Professional 1",
                offerType = 1,
                propertyType = "Type 1"
            ),
            Flat(
                id = 3,
                bedrooms = 4,
                rooms = 4,
                city = "City 2",
                area = 150.0,
                imageUrl = "url2",
                price = BigDecimal(150000),
                professional = "Professional 2",
                offerType = 2,
                propertyType = "Type 2"
            )
        )
        `when`(localDataSource.getAllFlatsAsFlow()).thenReturn(flowOf(localFlats))
        `when`(flatMappings.localToDomain()).thenReturn { localFlat: LocalFlat ->
            Flat(
                id = localFlat.id,
                bedrooms = localFlat.bedrooms,
                rooms = localFlat.rooms,
                city = localFlat.city,
                area = localFlat.area,
                imageUrl = localFlat.imageUrl,
                price = localFlat.price,
                professional = localFlat.professional,
                offerType = localFlat.offerType,
                propertyType = localFlat.propertyType
            )
        }

        val flatsFlow = flatRepository.getAllFlatsAsFlow().toList().first()

        assertEquals(flats, flatsFlow)
    }
}