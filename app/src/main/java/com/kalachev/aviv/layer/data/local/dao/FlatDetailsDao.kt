package com.kalachev.aviv.layer.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kalachev.aviv.layer.data.local.model.LocalFlatDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface FlatDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertFlatDetails(flatDetails: LocalFlatDetails)

    @Query("DELETE FROM flatdetails WHERE id IN (:ids)")
    fun deleteFlatDetailsByIds(ids: List<Long>)

    @Query("SELECT * FROM flatdetails WHERE id = :id")
    suspend fun getFlatDetailsById(id: Long): LocalFlatDetails?

    @Query("SELECT * FROM flatdetails WHERE id = :id")
    fun getFlatDetailsByIdAsFlow(id: Long): Flow<LocalFlatDetails>

}