package de.nilsdruyen.koncept.dogs.domain.usecase

import de.nilsdruyen.koncept.dogs.domain.repository.DogsRepository
import javax.inject.Inject

class UpdateFavoriteBreedUseCaseImpl @Inject constructor(val dogsRepository: DogsRepository) :
    UpdateFavoriteBreedUseCase {

    override suspend fun execute(breedId: Int, favorite: Boolean) {
        if (favorite) {
            dogsRepository.setFavorite(breedId)
        } else {
            dogsRepository.removeFavorite(breedId)
        }
    }
}
