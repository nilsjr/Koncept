package de.nilsdruyen.koncept.dogs.data

import arrow.core.Either
import de.nilsdruyen.koncept.domain.DataSourceError
import de.nilsdruyen.koncept.dogs.entities.Dog
import kotlinx.coroutines.flow.Flow

interface DogsCacheDataSource {
    suspend fun getDogList(): Flow<Either<DataSourceError, List<Dog>>>

    suspend fun setDogList(list: List<Dog>)
}