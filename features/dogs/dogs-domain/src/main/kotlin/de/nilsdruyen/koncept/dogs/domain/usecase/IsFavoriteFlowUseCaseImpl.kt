package de.nilsdruyen.koncept.dogs.domain.usecase

import de.nilsdruyen.koncept.dogs.domain.repository.DogsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsFavoriteFlowUseCaseImpl @Inject constructor(val dogsRepository: DogsRepository) : IsFavoriteFlowUseCase {

    override fun execute(breedId: Int): Flow<Boolean> {
        return dogsRepository.isFavoriteFlow(breedId)
    }
}