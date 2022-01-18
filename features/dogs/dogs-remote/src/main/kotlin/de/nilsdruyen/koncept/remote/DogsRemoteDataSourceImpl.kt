package de.nilsdruyen.koncept.remote

import arrow.core.Either
import de.nilsdruyen.koncept.dogs.data.DogsRemoteDataSource
import de.nilsdruyen.koncept.domain.DataSourceError
import de.nilsdruyen.koncept.entities.Dog
import de.nilsdruyen.koncept.remote.entities.DogWebEntity
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