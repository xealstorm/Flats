package com.kalachev.aviv.layer.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.kalachev.aviv.layer.data.local.model.LocalFlat
import com.kalachev.aviv.layer.data.local.model.LocalFlatIdPair
import kotlinx.coroutines.flow.Flow

@Dao
interface FlatDao {
    @Insert
    fun insertFlats(flats: List<LocalFlat>)

    @Update
    fun updateFlats(flats: List<LocalFlat>)

    @Delete
    fun deleteFlats(flats: List<LocalFlat>)

    @Query("DELETE FROM flats WHERE id = :id")
    suspend fun deleteFlatById(id: Long)

    @Query("SELECT * FROM flats")
    suspend fun getAllFlats():List<LocalFlat>

    @Query("SELECT * FROM flats")
    fun getAllFlatsAsFlow(): Flow<List<LocalFlat>>

    @Query("SELECT localId, id FROM flats")
    fun getLocalFlatIdPairs(): List<LocalFlatIdPair>


}