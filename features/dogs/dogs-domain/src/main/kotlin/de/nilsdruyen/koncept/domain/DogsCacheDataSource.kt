package de.nilsdruyen.koncept.domain

import arrow.core.Either
import kotlinx.coroutines.flow.Flow

interface DogsCacheDataSource {
    suspend fun getDogList(): Flow<Either<DataSourceError, List<Dog>>>

    suspend fun setDogList(list: List<Dog>)
}