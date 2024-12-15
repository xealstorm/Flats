package com.kalachev.aviv.layer.data.repo_impl

import com.kalachev.aviv.layer.data.local.LocalDataSource
import com.kalachev.aviv.layer.data.remote.RemoteDataSource
import com.kalachev.aviv.layer.domain.model.Flat
import com.kalachev.aviv.layer.domain.model.mapping.FlatMappings
import com.kalachev.aviv.layer.domain.repo.FlatRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class FlatRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val flatMappings: FlatMappings,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : FlatRepository {
    override suspend fun syncFlats(): Result<Unit> = withContext(ioDispatcher) {
        val freshFlatsResult = provideFlatsFromRemote()
        val freshFlats = freshFlatsResult.getOrNull()
        val existingFlats = provideFlatsFromLocal()
        if (freshFlatsResult.isSuccess && !freshFlats.isNullOrEmpty()) {
            val (flatsToAdd, flatsToModify, flatsToRemove) =
                compareFreshToExisting(freshFlats, existingFlats)
            localDataSource.updateFlatsAndFlatDetails(
                flatsToAdd.map { it.let(flatMappings.toData()) },
                flatsToModify.map { it.let(flatMappings.toData()) },
                flatsToRemove.map { it.let(flatMappings.toData()) }
            )
            Result.success(Unit)
        } else {
            Result.failure(freshFlatsResult.exceptionOrNull() ?: Exception("Failed to fetch flats"))
        }
    }

    override fun getAllFlatsAsFlow(): Flow<List<Flat>> =
        localDataSource.getAllFlatsAsFlow().map { flats ->
            flats.map { it.let(flatMappings.localToDomain()) }
        }

    private suspend fun provideFlatsFromLocal(): List<Flat> = withContext(ioDispatcher) {
        localDataSource.getAllFlats().map {
            it.let(flatMappings.localToDomain())
        }
    }

    private suspend fun provideFlatsFromRemote(): Result<List<Flat>> = withContext(ioDispatcher) {
        val result = remoteDataSource.getAllFlats()
        if (result.isSuccess) {
            Result.success(
                result.getOrNull()
                    ?.filterNotNull()
                    ?.mapNotNull {
                        it.let(flatMappings.remoteToDomain())
                    }
                    ?: listOf())
        } else {
            Result.failure(
                result.exceptionOrNull() ?: Exception("Failed to fetch assets")
            )
        }
    }

    /**
     * Compares two collections of flats and returns three collections as a result:
     * The first one contains elements that exist only in the fresh collection
     * The second one contains elements that exist in both collections but have differences in the inner fields
     * The third one contains elements that exist only in the existing collection
     */
    private fun compareFreshToExisting(
        fresh: List<Flat>,
        existing: List<Flat>
    ): Triple<List<Flat>, List<Flat>, List<Flat>> {
        val toAddOrUpdate = fresh.filter { freshItem ->
            existing.none { it.id == freshItem.id && it == freshItem }
        }

        val toRemove = existing.filter { existingItem ->
            fresh.none { it.id == existingItem.id }
        }

        val (toModify, toAdd) = toAddOrUpdate.partition { asset ->
            existing.any { it.id == asset.id }
        }
        return Triple(toAdd, toModify, toRemove)
    }
}