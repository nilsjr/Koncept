package de.nilsdruyen.koncept.dogs

import arrow.core.Either
import de.nilsdruyen.koncept.domain.DataSourceError
import kotlinx.coroutines.flow.Flow

interface DogsRepository {
    suspend fun getList(): Flow<Either<DataSourceError, List<Dog>>>
}