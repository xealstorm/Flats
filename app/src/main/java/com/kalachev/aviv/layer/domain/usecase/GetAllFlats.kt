package com.kalachev.aviv.layer.domain.usecase

import com.kalachev.aviv.layer.domain.model.Flat
import com.kalachev.aviv.layer.domain.repo.FlatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class GetAllFlats(
    private val flatRepository: FlatRepository,
) {
    operator fun invoke(): Flow<Result<List<Flat>>> = flatRepository.getAllFlatsAsFlow()
        .map { flats ->
            Result.success(flats)
        }
        .catch { e ->
            emit(Result.failure(e))
        }
}