package de.nilsdruyen.koncept.dogs.data

import arrow.core.Either
import de.nilsdruyen.koncept.domain.DataSourceError
import de.nilsdruyen.koncept.domain.Dog
import de.nilsdruyen.koncept.domain.DogsRepository
import de.nilsdruyen.koncept.domain.GetDogListUseCase
import de.nilsdruyen.koncept.domain.annotations.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetDogListUseCaseImpl @Inject constructor(val dogsRepository: DogsRepository) : GetDogListUseCase {

    override suspend fun execute(): Flow<Either<DataSourceError, List<Dog>>> = dogsRepository.getList()
}