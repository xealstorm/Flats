package com.kalachev.aviv.layer.domain.usecase

import com.kalachev.aviv.layer.domain.repo.FlatDetailsRepository

class SyncFlatDetails(
    private val flatDetailsRepository: FlatDetailsRepository,
) {
    suspend operator fun invoke(id: Long): Result<Unit> = flatDetailsRepository.syncFlatDetailsById(id)
}