package de.nilsdruyen.koncept.domain.repository

import arrow.core.Either
import de.nilsdruyen.koncept.domain.DataSourceError
import de.nilsdruyen.koncept.entities.Dog
import kotlinx.coroutines.flow.Flow

interface DogsRepository {
    suspend fun getList(): Flow<Either<DataSourceError, List<Dog>>>
}