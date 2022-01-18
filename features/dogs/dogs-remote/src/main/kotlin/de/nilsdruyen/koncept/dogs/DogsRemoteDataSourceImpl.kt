package de.nilsdruyen.koncept.dogs

import arrow.core.Either
import de.nilsdruyen.koncept.dogs.entities.DogWebEntity
import de.nilsdruyen.koncept.domain.DataSourceError
import javax.inject.Inject

class DogsRemoteDataSourceImpl @Inject constructor(
    private val dogsApi: DogsApi
) : DogsRemoteDataSource {

    override suspend fun getList(): Either<DataSourceError, List<Dog>> {
        return dogsApi.getBreeds().map {
            it.map(DogWebEntity::toModel)
        }
    }
}