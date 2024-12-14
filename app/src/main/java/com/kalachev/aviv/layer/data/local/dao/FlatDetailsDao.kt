package com.kalachev.aviv.layer.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.kalachev.aviv.layer.data.local.model.LocalFlatDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface FlatDetailsDao {

    @Insert
    fun insertFlatDetails(flatDetails: LocalFlatDetails)

    @Update
    fun updateFlatDetails(flatDetails: LocalFlatDetails)

    @Query("DELETE FROM flatdetails WHERE id IN (:ids)")
    fun deleteFlatDetailsByIds(ids: List<Long>)

    @Query("SELECT * FROM flatdetails WHERE id = :id")
    suspend fun getFlatDetailsById(id: Long): LocalFlatDetails?

    @Query("SELECT * FROM flatdetails WHERE id = :id")
    fun getFlatDetailsByIdAsFlow(id: Long): Flow<LocalFlatDetails?>

}