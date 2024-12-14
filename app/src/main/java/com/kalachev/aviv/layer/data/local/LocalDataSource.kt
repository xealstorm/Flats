package com.kalachev.aviv.layer.data.local

import com.kalachev.aviv.layer.data.local.dao.FlatDao
import com.kalachev.aviv.layer.data.local.dao.FlatDetailsDao
import com.kalachev.aviv.layer.data.local.model.LocalFlat
import com.kalachev.aviv.layer.data.local.model.LocalFlatDetails
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class LocalDataSource(
    private val appDatabase: AppDatabase,
    private val flatDao: FlatDao,
    private val flatDetailsDao: FlatDetailsDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    /**
     * Asynchronously provides a list of all flats stored locally
     */
    suspend fun getAllFlats(): List<LocalFlat> = flatDao.getAllFlats()

    /**
     * Provides a list of all flats stored locally in a format of a flow
     */
    fun getAllFlatsAsFlow(): Flow<List<LocalFlat>> = flatDao.getAllFlatsAsFlow()


    /**
     * Adds, updates and deletes the flats from the corresponding lists.
     * Also deletes the flat details of the updated and deleted flats.
     */
    suspend fun updateFlatsAndFlatDetails(
        flatsToAdd: List<LocalFlat>,
        flatsToUpdate: List<LocalFlat>,
        flatsToDelete: List<LocalFlat>,
    ) = withContext(ioDispatcher) {
        val flatDetailsIdsToDelete = flatsToUpdate.map { it.id } + flatsToDelete.map { it.id }
        appDatabase.runInTransaction {
            flatDao.insertFlats(flatsToAdd)
            flatDao.updateFlats(flatsToUpdate)
            flatDao.deleteFlats(flatsToDelete)
            flatDetailsDao.deleteFlatDetailsByIds(flatDetailsIdsToDelete)
        }

    }

    /**
     * Asynchronously provides flat details stored locally for a given ID
     */
    suspend fun getFlatDetailsById(id: Long): LocalFlatDetails? = flatDetailsDao.getFlatDetailsById(id)

    /**
     * Provides flat details stored locally for a given ID in a format of a flow
     */
    fun getFlatDetailsByIdAsFlow(id: Long): Flow<LocalFlatDetails?> = flatDetailsDao.getFlatDetailsByIdAsFlow(id)

}