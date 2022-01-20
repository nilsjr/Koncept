package de.nilsdruyen.koncept.dogs.domain.repository

import de.nilsdruyen.koncept.dogs.domain.BreedImages
import de.nilsdruyen.koncept.dogs.domain.DogListFlow

interface DogsRepository {
    suspend fun getList(): DogListFlow

    suspend fun getImagesForBreed(breedId: Int): BreedImages
}