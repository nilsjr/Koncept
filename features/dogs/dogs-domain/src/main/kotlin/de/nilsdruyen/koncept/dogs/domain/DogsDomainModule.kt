package de.nilsdruyen.koncept.dogs.domain

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.nilsdruyen.koncept.dogs.domain.usecase.GetBreedImageListUseCase
import de.nilsdruyen.koncept.dogs.domain.usecase.GetBreedImageListUseCaseImpl
import de.nilsdruyen.koncept.dogs.domain.usecase.GetDogListUseCase
import de.nilsdruyen.koncept.dogs.domain.usecase.GetDogListUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
internal interface DogsDomainModule {

    @Binds
    fun bindsGetDogListUseCase(useCase: GetDogListUseCaseImpl): GetDogListUseCase

    @Binds
    fun GetBreedImageListUseCaseImpl.bindGetBreedImageListUseCase(): GetBreedImageListUseCase
}