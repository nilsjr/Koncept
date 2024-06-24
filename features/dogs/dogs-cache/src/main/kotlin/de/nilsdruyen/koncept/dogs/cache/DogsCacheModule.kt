package de.nilsdruyen.koncept.dogs.cache

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.nilsdruyen.koncept.dogs.data.DogsCacheDataSource

@Module
@InstallIn(SingletonComponent::class)
interface DogsCacheModule {

    @Binds
    fun bindDogsCacheDataSource(dogsCacheDataSourceImpl: DogsCacheDataSourceImpl): DogsCacheDataSource
}
