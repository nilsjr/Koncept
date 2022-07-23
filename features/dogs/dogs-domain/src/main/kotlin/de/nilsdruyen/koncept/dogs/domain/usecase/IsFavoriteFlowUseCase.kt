package de.nilsdruyen.koncept.dogs.domain.usecase

import kotlinx.coroutines.flow.Flow

interface IsFavoriteFlowUseCase {

    fun execute(breedId: Int): Flow<Boolean>
}