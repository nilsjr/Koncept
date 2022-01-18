package de.nilsdruyen.koncept.dogs

import arrow.core.Either
import de.nilsdruyen.koncept.domain.DataSourceError
import kotlinx.coroutines.flow.Flow

interface DogsCacheDataSource {
    suspend fun getDogList(): Flow<Either<DataSourceError, List<Dog>>>

    suspend fun setDogList(list: List<Dog>)
}