package de.nilsdruyen.koncept.dogs.remote

import arrow.core.Either
import de.nilsdruyen.koncept.dogs.data.DogsRemoteDataSource
import de.nilsdruyen.koncept.dogs.entity.BreedImage
import de.nilsdruyen.koncept.dogs.entity.Dog
import de.nilsdruyen.koncept.dogs.remote.entities.BreedImageWebEntity
import de.nilsdruyen.koncept.dogs.remote.entities.DogWebEntity
import de.nilsdruyen.koncept.domain.DataSourceError
import de.nilsdruyen.koncept.domain.annotations.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DogsRemoteDataSourceImpl @Inject constructor(
    private val dogsApi: DogsApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : DogsRemoteDataSource {

    override suspend fun getList(): Either<DataSourceError, List<Dog>> = withContext(ioDispatcher) {
        dogsApi.getBreeds().map {
            it.map(DogWebEntity::toModel)
        }
    }

    override suspend fun getImagesForBreed(breedId: Int): Either<DataSourceError, List<BreedImage>> =
        withContext(ioDispatcher) {
            dogsApi.searchImagesForBreed(
                limit = 20,
//                1,
//                "ASC",
                breedId = breedId,
                "small"
            ).map {
                it.map(BreedImageWebEntity::toModel)
            }
        }

    override suspend fun getImage(imageId: String): Either<DataSourceError, BreedImage> = withContext(ioDispatcher) {
        dogsApi.getImage(imageId).map(BreedImageWebEntity::toModel)
    }
}
