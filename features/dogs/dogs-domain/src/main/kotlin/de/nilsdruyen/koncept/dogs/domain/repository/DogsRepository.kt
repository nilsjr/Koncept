package de.nilsdruyen.koncept.dogs.domain.repository

import arrow.core.Either
import de.nilsdruyen.koncept.dogs.entity.Dog
import de.nilsdruyen.koncept.domain.DataSourceError
import kotlinx.coroutines.flow.Flow

interface DogsRepository {
    suspend fun getList(): Flow<Either<DataSourceError, List<Dog>>>
}