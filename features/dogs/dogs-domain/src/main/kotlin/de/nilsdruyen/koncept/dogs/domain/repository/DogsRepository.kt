package de.nilsdruyen.koncept.dogs.domain.repository

import arrow.core.Either
import de.nilsdruyen.koncept.domain.DataSourceError
import de.nilsdruyen.koncept.dogs.entities.Dog



import kotlinx.coroutines.flow.Flow

interface DogsRepository {
    suspend fun getList(): Flow<Either<DataSourceError, List<Dog>>>
}