package com.kalachev.aviv.layer.domain.repo

import com.kalachev.aviv.layer.domain.model.Flat
import kotlinx.coroutines.flow.Flow

interface FlatRepository {
    suspend fun syncFlats(): Result<Unit>

    fun getAllFlatsAsFlow(): Flow<List<Flat>>
}