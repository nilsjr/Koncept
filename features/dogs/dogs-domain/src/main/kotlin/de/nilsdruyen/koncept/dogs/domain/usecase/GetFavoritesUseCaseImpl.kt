package de.nilsdruyen.koncept.dogs.domain.usecase

import arrow.core.Either
import de.nilsdruyen.koncept.dogs.domain.repository.DogsRepository
import de.nilsdruyen.koncept.dogs.entity.Dog
import de.nilsdruyen.koncept.domain.DataSourceError
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoritesUseCaseImpl @Inject constructor(
    val dogsRepository: DogsRepository,
) : GetFavoritesUseCase {

    override fun execute(): Flow<Either<DataSourceError, List<Dog>>> {
        return dogsRepository.getFavoriteList()
    }
}
