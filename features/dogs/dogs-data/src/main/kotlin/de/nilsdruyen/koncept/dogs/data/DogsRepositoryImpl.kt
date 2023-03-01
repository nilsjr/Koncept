package de.nilsdruyen.koncept.dogs.data

import arrow.core.Either
import de.nilsdruyen.koncept.dogs.domain.BreedImages
import de.nilsdruyen.koncept.dogs.domain.repository.DogsRepository
import de.nilsdruyen.koncept.dogs.entity.Dog
import de.nilsdruyen.koncept.domain.DataSourceError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DogsRepositoryImpl @Inject constructor(
    private val dogsRemoteDataSource: DogsRemoteDataSource,
    private val dogsCacheDataSource: DogsCacheDataSource,
) : DogsRepository {

    override fun getList(): Flow<Either<DataSourceError, List<Dog>>> {
        return flow {
            val cache = dogsCacheDataSource.getDogList().firstOrNull()
            if (cache != null) emit(cache)
            emit(
                dogsRemoteDataSource.getList().onRight {
                    dogsCacheDataSource.setDogList(it)
                }
            )
            emitAll(dogsCacheDataSource.getDogList())
        }.map {
            println("emit $it")
            it
        }.distinctUntilChanged()
    }

    override suspend fun getImagesForBreed(breedId: Int): BreedImages {
        return dogsRemoteDataSource.getImagesForBreed(breedId)
    }

    override fun getFavoriteList(): Flow<Either<DataSourceError, List<Dog>>> {
        return dogsCacheDataSource.getFavorites()
    }

    override suspend fun setFavorite(breedId: Int) {
        dogsCacheDataSource.setFavorite(breedId)
    }

    override suspend fun removeFavorite(breedId: Int) {
        dogsCacheDataSource.removeFavorite(breedId)
    }

    override fun isFavoriteFlow(breedId: Int): Flow<Boolean> {
        return dogsCacheDataSource.isFavoriteFlow(breedId)
    }
}