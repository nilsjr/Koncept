package de.nilsdruyen.koncept.dogs.data

import arrow.core.Either
import de.nilsdruyen.koncept.domain.DataSourceError
import de.nilsdruyen.koncept.dogs.entities.Dog

interface DogsRemoteDataSource {
    suspend fun getList(): Either<DataSourceError, List<Dog>>
}