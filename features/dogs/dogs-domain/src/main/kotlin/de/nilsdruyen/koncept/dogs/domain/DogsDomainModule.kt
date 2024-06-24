package de.nilsdruyen.koncept.dogs.domain

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.nilsdruyen.koncept.dogs.domain.usecase.GetBreedImageListUseCase
import de.nilsdruyen.koncept.dogs.domain.usecase.GetBreedImageListUseCaseImpl
import de.nilsdruyen.koncept.dogs.domain.usecase.GetDogListUseCase
import de.nilsdruyen.koncept.dogs.domain.usecase.GetDogListUseCaseImpl
import de.nilsdruyen.koncept.dogs.domain.usecase.GetFavoritesUseCase
import de.nilsdruyen.koncept.dogs.domain.usecase.GetFavoritesUseCaseImpl
import de.nilsdruyen.koncept.dogs.domain.usecase.IsFavoriteFlowUseCase
import de.nilsdruyen.koncept.dogs.domain.usecase.IsFavoriteFlowUseCaseImpl
import de.nilsdruyen.koncept.dogs.domain.usecase.UpdateFavoriteBreedUseCase
import de.nilsdruyen.koncept.dogs.domain.usecase.UpdateFavoriteBreedUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
internal interface DogsDomainModule {

    @Binds
    fun bindsGetDogListUseCase(useCase: GetDogListUseCaseImpl): GetDogListUseCase

    @Binds
    fun bindGetBreedImageListUseCase(
        getBreedImageListUseCaseImpl: GetBreedImageListUseCaseImpl
    ): GetBreedImageListUseCase

    @Binds
    fun bindUpdateFavoriteBreedUseCase(
        updateFavoriteBreedUseCaseImpl: UpdateFavoriteBreedUseCaseImpl
    ): UpdateFavoriteBreedUseCase

    @Binds
    fun bindIsFavoriteFlowUseCase(isFavoriteFlowUseCaseImpl: IsFavoriteFlowUseCaseImpl): IsFavoriteFlowUseCase

    @Binds
    fun bindGetFavoritesUseCase(getFavoritesUseCaseImpl: GetFavoritesUseCaseImpl): GetFavoritesUseCase
}
