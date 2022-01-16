package de.nilsdruyen.koncept.domain

import arrow.core.Either
import kotlinx.coroutines.flow.Flow

interface GetDogListUseCase {

    suspend fun execute(): Flow<Either<DataSourceError, List<Dog>>>
}