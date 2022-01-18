package de.nilsdruyen.koncept.domain.usecase

import arrow.core.Either
import de.nilsdruyen.koncept.domain.DataSourceError
import de.nilsdruyen.koncept.domain.repository.DogsRepository
import de.nilsdruyen.koncept.entities.Dog
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetDogListUseCaseImpl @Inject constructor(
    private val dogsRepository: DogsRepository
) : GetDogListUseCase {

    override suspend fun execute(): Flow<Either<DataSourceError, List<Dog>>> = dogsRepository.getList()
}