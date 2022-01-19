package de.nilsdruyen.koncept.dogs.domain.usecase

import arrow.core.Either
import arrow.core.computations.either
import de.nilsdruyen.koncept.dogs.domain.repository.DogsRepository
import de.nilsdruyen.koncept.domain.DataSourceError
import de.nilsdruyen.koncept.domain.Logger
import javax.inject.Inject

class GetListPositionUseCase @Inject constructor(
    val dogsRepository: DogsRepository
) {

    suspend fun execute(listId: String): Either<DataSourceError, Pair<Int, Int>> {
        return Either.Right(dogsRepository.getListPosition())
    }
}