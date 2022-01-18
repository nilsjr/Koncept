package de.nilsdruyen.koncept.dogs.data

import arrow.core.Either
import de.nilsdruyen.koncept.domain.DataSourceError
import de.nilsdruyen.koncept.entities.Dog

interface DogsRemoteDataSource {
    suspend fun getList(): Either<DataSourceError, List<Dog>>
}