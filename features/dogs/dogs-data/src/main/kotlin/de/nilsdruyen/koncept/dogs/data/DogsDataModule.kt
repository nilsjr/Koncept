package de.nilsdruyen.koncept.dogs.data

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.nilsdruyen.koncept.dogs.domain.repository.DogsRepository

@Module
@InstallIn(SingletonComponent::class)
interface DogsDataModule {

    @Binds
    fun bindDogsRepository(dogsRepositoryImpl: DogsRepositoryImpl): DogsRepository
}
