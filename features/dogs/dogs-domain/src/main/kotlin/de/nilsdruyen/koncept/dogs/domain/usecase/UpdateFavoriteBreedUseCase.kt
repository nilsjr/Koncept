package de.nilsdruyen.koncept.dogs.domain.usecase

interface UpdateFavoriteBreedUseCase {

    suspend fun execute(breedId: Int, favorite: Boolean)
}