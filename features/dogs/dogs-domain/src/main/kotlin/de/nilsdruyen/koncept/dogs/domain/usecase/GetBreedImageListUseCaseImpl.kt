package de.nilsdruyen.koncept.dogs.domain.usecase

import arrow.core.Either
import de.nilsdruyen.koncept.dogs.domain.repository.DogsRepository
import de.nilsdruyen.koncept.dogs.entity.BreedImage
import de.nilsdruyen.koncept.domain.DataSourceError
import javax.inject.Inject

class GetBreedImageListUseCaseImpl @Inject constructor(private val dogsRepository: DogsRepository) :
    GetBreedImageListUseCase {

    override suspend fun execute(breedId: Int): Either<DataSourceError, List<BreedImage>> =
        dogsRepository.getImagesForBreed(breedId)
}
