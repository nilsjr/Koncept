package de.nilsdruyen.koncept.domain

import arrow.core.Either

interface DogsRemoteDataSource {
    suspend fun getList(): Either<DataSourceError, List<Dog>>
}