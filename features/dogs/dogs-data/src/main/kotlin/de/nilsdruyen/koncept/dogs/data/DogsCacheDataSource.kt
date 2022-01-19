package de.nilsdruyen.koncept.dogs.data

import arrow.core.Either
import de.nilsdruyen.koncept.dogs.entity.Dog
import de.nilsdruyen.koncept.domain.DataSourceError
import kotlinx.coroutines.flow.Flow

interface DogsCacheDataSource {
    suspend fun getDogList(): Flow<Either<DataSourceError, List<Dog>>>

    suspend fun setDogList(list: List<Dog>)

    suspend fun saveListPosition(index: Int, offset: Int)

    suspend fun getListPosition(): Pair<Int, Int>
}