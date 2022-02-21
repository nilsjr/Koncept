package de.nilsdruyen.koncept.dogs.remote

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.nilsdruyen.koncept.dogs.data.DogsRemoteDataSource
import retrofit2.Retrofit
import retrofit2.create

@Module
@InstallIn(SingletonComponent::class)
interface DogsRemoteModule {

    @Binds
    fun DogsRemoteDataSourceImpl.bindDogsRemoteDataSource(): DogsRemoteDataSource

    companion object {

        @Provides
        fun Retrofit.provideDogApi(): DogsApi = create()
    }
}