package de.nilsdruyen.koncept.dogs.domain.usecase

import arrow.core.Either
import de.nilsdruyen.koncept.dogs.entity.Dog
import de.nilsdruyen.koncept.domain.DataSourceError
import kotlinx.coroutines.flow.Flow

interface GetFavoritesUseCase {

    fun execute(): Flow<Either<DataSourceError, List<Dog>>>
}