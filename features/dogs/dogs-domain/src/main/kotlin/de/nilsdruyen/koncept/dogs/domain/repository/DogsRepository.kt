package de.nilsdruyen.koncept.dogs.domain.repository

import arrow.core.Either
import de.nilsdruyen.koncept.dogs.domain.BreedImages
import de.nilsdruyen.koncept.dogs.domain.DogListFlow
import de.nilsdruyen.koncept.dogs.entity.Dog
import de.nilsdruyen.koncept.domain.DataSourceError
import kotlinx.coroutines.flow.Flow

interface DogsRepository {

    fun getList(): DogListFlow

    suspend fun getImagesForBreed(breedId: Int): BreedImages

    fun getFavoriteList(): Flow<Either<DataSourceError, List<Dog>>>

    suspend fun setFavorite(breedId: Int)

    suspend fun removeFavorite(breedId: Int)

    fun isFavoriteFlow(breedId: Int): Flow<Boolean>
}