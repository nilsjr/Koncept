package de.nilsdruyen.koncept.domain

import arrow.core.Either
import kotlinx.coroutines.flow.Flow

interface DogsRepository {
    suspend fun getList(): Flow<Either<DataSourceError, List<Dog>>>
}