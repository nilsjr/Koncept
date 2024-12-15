package de.nilsdruyen.koncept.dogs.domain

import arrow.core.Either
import de.nilsdruyen.koncept.dogs.entity.Breed
import de.nilsdruyen.koncept.dogs.entity.BreedImage
import de.nilsdruyen.koncept.domain.DataSourceError
import kotlinx.coroutines.flow.Flow

typealias DogListFlow = Flow<Either<DataSourceError, List<Breed>>>
typealias BreedImages = Either<DataSourceError, List<BreedImage>>
