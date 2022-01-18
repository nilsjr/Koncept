package de.nilsdruyen.koncept.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.nilsdruyen.koncept.dogs.DogsCacheDataSource
import de.nilsdruyen.koncept.dogs.DogsCacheDataSourceImpl
import de.nilsdruyen.koncept.dogs.DogsRemoteDataSource
import de.nilsdruyen.koncept.dogs.DogsRemoteDataSourceImpl
import de.nilsdruyen.koncept.dogs.DogsRepository
import de.nilsdruyen.koncept.dogs.DogsRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
interface DogsModule {

    @Binds
    fun DogsRepositoryImpl.bindDogsRepository(): DogsRepository

    @Binds
    fun DogsCacheDataSourceImpl.bindDogsCacheDataSource(): DogsCacheDataSource

    @Binds
    fun DogsRemoteDataSourceImpl.bindDogsRemoteDataSource(): DogsRemoteDataSource
}