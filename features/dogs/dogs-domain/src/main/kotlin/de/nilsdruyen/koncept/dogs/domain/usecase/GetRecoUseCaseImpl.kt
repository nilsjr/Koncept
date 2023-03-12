package de.nilsdruyen.koncept.dogs.domain.usecase

import de.nilsdruyen.koncept.dogs.domain.repository.DogsRepository
import de.nilsdruyen.koncept.dogs.entity.Dog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetRecoUseCaseImpl @Inject constructor(
    val dogsRepository: DogsRepository,
) : GetRecoUseCase {

    override fun execute(): Flow<List<Dog>> {
        return dogsRepository.getList().map {
            it.getOrNull().orEmpty().filter { dog ->
                dog.name.startsWith("m", ignoreCase = true)
            }
        }
    }
}