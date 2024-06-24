package de.nilsdruyen.koncept.dogs.testing

import arrow.core.Either
import arrow.core.right
import de.nilsdruyen.koncept.dogs.remote.DogsApi
import de.nilsdruyen.koncept.dogs.remote.entities.BreedImageWebEntity
import de.nilsdruyen.koncept.dogs.remote.entities.DogWebEntity
import de.nilsdruyen.koncept.domain.DataSourceError
import de.nilsdruyen.koncept.test.utils.parseList

class FakeDogsApi : DogsApi {

    override suspend fun getBreeds(): Either<DataSourceError, List<DogWebEntity>> {
        val dogEntityList = "/json/dog-list.json".parseList(DogWebEntity::class.java).take(25)
        return dogEntityList.right()
    }

    override suspend fun searchBreed(input: String): Either<DataSourceError, List<DogWebEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun searchImages(
        limit: Int,
        page: Int,
        order: String
    ): Either<DataSourceError, List<BreedImageWebEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun searchImagesForBreed(
        limit: Int,
        breedId: Int,
        size: String
    ): Either<DataSourceError, List<BreedImageWebEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun getImage(id: String): Either<DataSourceError, BreedImageWebEntity> {
        TODO("Not yet implemented")
    }
}
