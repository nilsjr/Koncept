package de.nilsdruyen.koncept.dogs.data

import arrow.core.Either
import de.nilsdruyen.koncept.dogs.entity.Dog
import de.nilsdruyen.koncept.domain.DataSourceError

interface DogsRemoteDataSource {
    suspend fun getList(): Either<DataSourceError, List<Dog>>
}