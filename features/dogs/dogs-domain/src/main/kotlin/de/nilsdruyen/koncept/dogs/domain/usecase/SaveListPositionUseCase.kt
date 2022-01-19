package de.nilsdruyen.koncept.dogs.domain.usecase

import de.nilsdruyen.koncept.dogs.domain.repository.DogsRepository
import de.nilsdruyen.koncept.domain.Logger
import javax.inject.Inject

class SaveListPositionUseCase @Inject constructor(
    private val dogsRepository: DogsRepository
) {

    suspend fun execute(listName: String, index: Int, offset: Int) {
        return dogsRepository.saveListPosition(index, offset).also {
            Logger.log("save position $index/$offset")
        }
    }
}