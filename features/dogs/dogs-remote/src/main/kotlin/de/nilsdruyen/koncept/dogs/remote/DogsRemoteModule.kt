package de.nilsdruyen.koncept.dogs.remote

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.nilsdruyen.koncept.dogs.data.DogsRemoteDataSource
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DogsRemoteModule {

    @Binds
    @Singleton
    fun DogsRemoteDataSourceImpl.bindDogsRemoteDataSource(): DogsRemoteDataSource
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DogsRemoteStaticModule {

    @Provides
    @Singleton
    fun Retrofit.provideDogApi(): DogsApi = create()
}