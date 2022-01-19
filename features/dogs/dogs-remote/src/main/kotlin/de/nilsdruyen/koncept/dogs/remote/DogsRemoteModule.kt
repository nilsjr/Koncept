package de.nilsdruyen.koncept.dogs.remote

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.nilsdruyen.koncept.dogs.data.DogsRemoteDataSource

@Module
@InstallIn(SingletonComponent::class)
interface DogsRemoteModule {

    @Binds
    fun DogsRemoteDataSourceImpl.bindDogsRemoteDataSource(): DogsRemoteDataSource
}