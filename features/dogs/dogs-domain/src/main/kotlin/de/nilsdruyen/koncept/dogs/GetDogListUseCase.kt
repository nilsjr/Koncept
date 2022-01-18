package de.nilsdruyen.koncept.dogs

import arrow.core.Either
import de.nilsdruyen.koncept.domain.DataSourceError
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDogListUseCase @Inject constructor(private val dogsRepository: DogsRepository) {

    suspend operator fun invoke(): Flow<Either<DataSourceError, List<Dog>>> = dogsRepository.getList()
}