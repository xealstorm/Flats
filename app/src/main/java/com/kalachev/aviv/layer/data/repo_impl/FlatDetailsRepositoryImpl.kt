package com.kalachev.aviv.layer.data.repo_impl

import com.kalachev.aviv.layer.data.local.LocalDataSource
import com.kalachev.aviv.layer.data.remote.RemoteDataSource
import com.kalachev.aviv.layer.domain.model.FlatDetails
import com.kalachev.aviv.layer.domain.model.mappings.FlatDetailsMappings
import com.kalachev.aviv.layer.domain.repo.FlatDetailsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class FlatDetailsRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val flatDetailsMappings: FlatDetailsMappings,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : FlatDetailsRepository {

    override fun getFlatDetailsByIdAsFlow(id: Long): Flow<FlatDetails> =
        localDataSource.getFlatDetailsByIdAsFlow(id).map { it.let(flatDetailsMappings.localToDomain()) }

    override suspend fun syncFlatDetailsById(id: Long): Result<Unit> = withContext(ioDispatcher) {
        val freshFlatDetailsResult = provideFlatDetailsByIdFromRemote(id)
        val freshFlatDetails = freshFlatDetailsResult.getOrNull()
        val existingFlatDetails = provideFlatDetailsByIdFromLocal(id)
        if (freshFlatDetailsResult.isSuccess && freshFlatDetails != null) {
            if (existingFlatDetails != freshFlatDetails) {
                localDataSource.updateFlatDetails(freshFlatDetails.let(flatDetailsMappings.toData()))
            }
            Result.success(Unit)
        } else {
            Result.failure(freshFlatDetailsResult.exceptionOrNull() ?: Exception("Failed to fetch assets"))
        }
    }

    private suspend fun provideFlatDetailsByIdFromLocal(id: Long): FlatDetails? = withContext(ioDispatcher) {
        localDataSource.getFlatDetailsById(id)?.let(flatDetailsMappings.localToDomain())
    }

    private suspend fun provideFlatDetailsByIdFromRemote(id: Long): Result<FlatDetails?> = withContext(ioDispatcher) {
        val result = remoteDataSource.getFlatDetailsById(id)
        if (result.isSuccess) {
            Result.success(result.getOrNull()?.let(flatDetailsMappings.remoteToDomain()))
        } else {
            Result.failure(
                result.exceptionOrNull() ?: Exception("Failed to fetch flat details")
            )
        }
    }
}
