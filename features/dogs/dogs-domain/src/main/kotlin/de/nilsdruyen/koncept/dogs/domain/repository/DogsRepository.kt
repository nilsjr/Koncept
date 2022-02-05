package de.nilsdruyen.koncept.dogs.domain.repository

import androidx.paging.PagingData
import arrow.core.Either
import de.nilsdruyen.koncept.dogs.domain.BreedImages
import de.nilsdruyen.koncept.dogs.domain.DogListFlow
import de.nilsdruyen.koncept.dogs.entity.Breed
import de.nilsdruyen.koncept.dogs.entity.BreedImage
import de.nilsdruyen.koncept.domain.DataSourceError
import kotlinx.coroutines.flow.Flow

interface DogsRepository {
    suspend fun getList(): DogListFlow

    suspend fun getImagesForBreed(breedId: Int): BreedImages

    suspend fun getBreeds(): Flow<PagingData<Breed>>
}