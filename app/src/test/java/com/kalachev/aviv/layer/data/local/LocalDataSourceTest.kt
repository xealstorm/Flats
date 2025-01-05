package com.kalachev.aviv.layer.data.local

import com.kalachev.aviv.layer.data.local.dao.FlatDao
import com.kalachev.aviv.layer.data.local.dao.FlatDetailsDao
import com.kalachev.aviv.layer.data.local.model.LocalFlat
import com.kalachev.aviv.layer.data.local.model.LocalFlatDetails
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyList
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import java.math.BigDecimal

class LocalDataSourceTest {
    private lateinit var flatDao: FlatDao
    private lateinit var flatDetailsDao: FlatDetailsDao
    private lateinit var localDataSource: LocalDataSource

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        flatDao = mock(FlatDao::class.java)
        flatDetailsDao = mock(FlatDetailsDao::class.java)
        localDataSource =
            LocalDataSource(mock(AppDatabase::class.java), flatDao, flatDetailsDao, UnconfinedTestDispatcher())
    }

    @Test
    fun `getAllFlats provides the correct LocalFlats`() = runBlocking {
        val flats = listOf(
            LocalFlat(
                id = 1,
                localId = 1,
                bedrooms = 2,
                rooms = 3,
                city = "City",
                area = 100.0,
                imageUrl = null,
                price = BigDecimal(1000),
                professional = "Pro",
                offerType = 1,
                propertyType = "Type"
            )
        )
        `when`(flatDao.getAllFlats()).thenReturn(flats)

        val result = localDataSource.getAllFlats()
        assertEquals(flats, result)
    }

    @Test
    fun `getAllFlatsAsFlow provides the correct flow of LocalFlats`() = runTest {
        val flats = listOf(
            LocalFlat(
                id = 1,
                localId = 1,
                bedrooms = 2,
                rooms = 3,
                city = "City",
                area = 100.0,
                imageUrl = null,
                price = BigDecimal(1000),
                professional = "Pro",
                offerType = 1,
                propertyType = "Type"
            )
        )
        `when`(flatDao.getAllFlatsAsFlow()).thenReturn(flowOf(flats))

        val result = localDataSource.getAllFlatsAsFlow()
        assertEquals(flats, result.first())
    }

    @Test
    fun `getFlatDetailsById returns the correct LocalFlatDetails`() = runBlocking {
        val flatDetails = LocalFlatDetails(
            id = 1,
            localId = 2,
            bedrooms = 3,
            rooms = 4,
            city = "City2",
            area = 200.0,
            imageUrl = null,
            price = BigDecimal(2000),
            professional = "Pro2",
            offerType = 2,
            propertyType = "Type2"
        )
        `when`(flatDetailsDao.getFlatDetailsById(1)).thenReturn(flatDetails)

        val result = localDataSource.getFlatDetailsById(1)
        assertEquals(flatDetails, result)
    }

    @Test
    fun `getFlatDetailsByIdAsFlow provides the correct flow of LocalFlatDetails`() = runTest {
        val flatDetails = LocalFlatDetails(
            id = 2,
            localId = 2,
            bedrooms = 3,
            rooms = 4,
            city = "City2",
            area = 200.0,
            imageUrl = null,
            price = BigDecimal(2000),
            professional = "Pro2",
            offerType = 2,
            propertyType = "Type2"
        )
        `when`(flatDetailsDao.getFlatDetailsByIdAsFlow(1)).thenReturn(flowOf(flatDetails))

        val result = localDataSource.getFlatDetailsByIdAsFlow(1)
        assertEquals(flatDetails, result.first())
    }

    @Test
    fun `updateFlatDetails updates the LocalFlatDetails`() = runBlocking {
        val flatDetails = LocalFlatDetails(
            id = 2,
            localId = 2,
            bedrooms = 3,
            rooms = 4,
            city = "City2",
            area = 200.0,
            imageUrl = null,
            price = BigDecimal(2000),
            professional = "Pro2",
            offerType = 2,
            propertyType = "Type2"
        )

        localDataSource.updateFlatDetails(flatDetails)

        verify(flatDetailsDao).upsertFlatDetails(flatDetails)
    }

    @Test
    fun `getAllFlats returns an empty list when the database is empty`() = runBlocking {
        `when`(flatDao.getAllFlats()).thenReturn(emptyList())

        val result = localDataSource.getAllFlats()
        assertTrue(result.isEmpty())
    }

    @Test
    fun `getFlatDetailsById returns null when the id is incorrect`() = runBlocking {
        `when`(flatDetailsDao.getFlatDetailsById(-1)).thenReturn(null)

        val result = localDataSource.getFlatDetailsById(-1)
        assertNull(result)
    }

    @Test
    fun `updateFlatsAndFlatDetails doesn't perform any changes when the data is incorrect`() = runBlocking {
        val flatsToAdd = listOf(
            LocalFlat(
                id = -1,
                localId = -1,
                bedrooms = 0,
                rooms = 0,
                city = "",
                area = 0.0,
                imageUrl = null,
                price = null,
                professional = "",
                offerType = 0,
                propertyType = ""
            )
        )
        val flatsToUpdate = listOf(
            LocalFlat(
                id = -1,
                localId = -1,
                bedrooms = 0,
                rooms = 0,
                city = "",
                area = 0.0,
                imageUrl = null,
                price = null,
                professional = "",
                offerType = 0,
                propertyType = ""
            )
        )
        val flatsToDelete = listOf(
            LocalFlat(
                id = -1,
                localId = -1,
                bedrooms = 0,
                rooms = 0,
                city = "",
                area = 0.0,
                imageUrl = null,
                price = null,
                professional = "",
                offerType = 0,
                propertyType = ""
            )
        )

        `when`(flatDao.getLocalFlatIdPairs()).thenReturn(emptyList())

        localDataSource.updateFlatsAndFlatDetails(flatsToAdd, flatsToUpdate, flatsToDelete)

        verify(flatDao, never()).insertFlats(anyList())
        verify(flatDao, never()).updateFlats(anyList())
        verify(flatDao, never()).deleteFlats(anyList())
        verify(flatDetailsDao, never()).deleteFlatDetailsByIds(anyList())
    }
}