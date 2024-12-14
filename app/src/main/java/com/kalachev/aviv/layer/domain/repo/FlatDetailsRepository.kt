package com.kalachev.aviv.layer.domain.repo

import com.kalachev.aviv.layer.domain.model.FlatDetails
import kotlinx.coroutines.flow.Flow

interface FlatDetailsRepository {

    suspend fun syncFlatDetailsById(id: Long): Result<Unit>

    fun getFlatDetailsByIdAsFlow(id: Long): Flow<FlatDetails>
}