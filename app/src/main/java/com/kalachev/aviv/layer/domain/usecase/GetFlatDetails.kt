package com.kalachev.aviv.layer.domain.usecase

import com.kalachev.aviv.layer.domain.model.FlatDetails
import com.kalachev.aviv.layer.domain.repo.FlatDetailsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class GetFlatDetails(
    private val flatDetailsRepository: FlatDetailsRepository,
) {
    operator fun invoke(id: Long): Flow<Result<FlatDetails>> = flatDetailsRepository.getFlatDetailsByIdAsFlow(id)
        .map { flatDetails ->
            Result.success(flatDetails)
        }
        .catch { e ->
            emit(Result.failure(e))
        }
}