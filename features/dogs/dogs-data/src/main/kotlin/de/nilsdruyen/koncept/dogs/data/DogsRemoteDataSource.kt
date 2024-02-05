package de.nilsdruyen.koncept.dogs.data

import arrow.core.Either
import de.nilsdruyen.koncept.dogs.entity.BreedImage
import de.nilsdruyen.koncept.dogs.entity.Dog
import de.nilsdruyen.koncept.domain.DataSourceError

interface DogsRemoteDataSource {
    suspend fun getList(): Either<DataSourceError, List<Dog>>

    suspend fun getImagesForBreed(breedId: Int): Either<DataSourceError, List<BreedImage>>

    suspend fun getImage(imageId: String): Either<DataSourceError, BreedImage>
}
