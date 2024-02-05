package de.nilsdruyen.koncept.dogs.data

import arrow.core.Either
import de.nilsdruyen.koncept.dogs.entity.Dog
import de.nilsdruyen.koncept.domain.DataSourceError
import kotlinx.coroutines.flow.Flow

interface DogsCacheDataSource {

    fun getDogList(): Flow<Either<DataSourceError, List<Dog>>>

    suspend fun setDogList(list: List<Dog>)

    suspend fun setFavorite(breedId: Int)

    suspend fun removeFavorite(breedId: Int)

    fun isFavoriteFlow(breedId: Int): Flow<Boolean>

    fun getFavorites(): Flow<Either<DataSourceError, List<Dog>>>
}
