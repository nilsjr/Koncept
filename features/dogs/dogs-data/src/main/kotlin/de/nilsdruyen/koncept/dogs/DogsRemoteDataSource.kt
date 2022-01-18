package de.nilsdruyen.koncept.dogs

import arrow.core.Either
import de.nilsdruyen.koncept.domain.DataSourceError

interface DogsRemoteDataSource {
    suspend fun getList(): Either<DataSourceError, List<Dog>>
}