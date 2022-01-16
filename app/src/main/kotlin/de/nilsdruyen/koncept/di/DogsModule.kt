package de.nilsdruyen.koncept.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.nilsdruyen.koncept.dog.cache.DogsCacheDataSourceImpl
import de.nilsdruyen.koncept.dogs.data.DogsRepositoryImpl
import de.nilsdruyen.koncept.dogs.data.GetDogListUseCaseImpl
import de.nilsdruyen.koncept.domain.DogsCacheDataSource
import de.nilsdruyen.koncept.domain.DogsRemoteDataSource
import de.nilsdruyen.koncept.domain.DogsRepository
import de.nilsdruyen.koncept.domain.GetDogListUseCase
import de.nilsdruyen.koncept.remote.DogsRemoteDataSourceImpl

@Module
@InstallIn(SingletonComponent::class)
interface DogsModule {

    @Binds
    fun GetDogListUseCaseImpl.bindGetDogListUseCase(): GetDogListUseCase

    @Binds
    fun DogsRepositoryImpl.bindDogsRepository(): DogsRepository

    @Binds
    fun DogsCacheDataSourceImpl.bindDogsCacheDataSource(): DogsCacheDataSource

    @Binds
    fun DogsRemoteDataSourceImpl.bindDogsRemoteDataSource(): DogsRemoteDataSource
}