package com.kalachev.aviv.layer.domain.usecase

import com.kalachev.aviv.layer.domain.repo.FlatRepository

class SyncFlats(
    private val flatRepository: FlatRepository,
) {
    suspend operator fun invoke(): Result<Unit> = flatRepository.syncFlats()
}