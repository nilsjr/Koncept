package de.nilsdruyen.koncept.domain.usecase

import arrow.core.Either
import de.nilsdruyen.koncept.domain.DataSourceError
import de.nilsdruyen.koncept.entities.Dog
import kotlinx.coroutines.flow.Flow

interface GetDogListUseCase {

    suspend fun execute(): Flow<Either<DataSourceError, List<Dog>>>
}