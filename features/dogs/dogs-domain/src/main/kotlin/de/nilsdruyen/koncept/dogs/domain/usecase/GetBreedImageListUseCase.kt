package de.nilsdruyen.koncept.dogs.domain.usecase

import arrow.core.Either
import de.nilsdruyen.koncept.dogs.entity.BreedImage
import de.nilsdruyen.koncept.domain.DataSourceError

interface GetBreedImageListUseCase {
    suspend fun execute(breedId: Int): Either<DataSourceError, List<BreedImage>>
}
