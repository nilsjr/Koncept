package de.nilsdruyen.koncept.dogs.domain.usecase

import de.nilsdruyen.koncept.dogs.entity.Dog
import kotlinx.coroutines.flow.Flow

interface GetRecoUseCase {

    fun execute(): Flow<List<Dog>>
}