package de.nilsdruyen.koncept.domain

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.nilsdruyen.koncept.domain.usecase.GetDogListUseCase
import de.nilsdruyen.koncept.domain.usecase.GetDogListUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
internal interface DogsDomainModule {

    @Binds
    fun bindsGetDogListUseCase(useCase: GetDogListUseCaseImpl): GetDogListUseCase
}