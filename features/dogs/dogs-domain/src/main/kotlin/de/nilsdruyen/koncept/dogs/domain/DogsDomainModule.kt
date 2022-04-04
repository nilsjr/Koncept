package de.nilsdruyen.koncept.dogs.domain

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.nilsdruyen.koncept.dogs.domain.usecase.GetBreedImageListUseCase
import de.nilsdruyen.koncept.dogs.domain.usecase.GetBreedImageListUseCaseImpl
import de.nilsdruyen.koncept.dogs.domain.usecase.GetDogListUseCase
import de.nilsdruyen.koncept.dogs.domain.usecase.GetDogListUseCaseImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface DogsDomainModule {

    @Binds
    @Singleton
    fun bindsGetDogListUseCase(useCase: GetDogListUseCaseImpl): GetDogListUseCase

    @Binds
    @Singleton
    fun GetBreedImageListUseCaseImpl.bindGetBreedImageListUseCase(): GetBreedImageListUseCase
}