package de.nilsdruyen.koncept.dogs.data

import arrow.core.Either
import arrow.core.computations.ResultEffect.bind
import de.nilsdruyen.koncept.dogs.domain.BreedImages
import de.nilsdruyen.koncept.dogs.domain.repository.DogsRepository
import de.nilsdruyen.koncept.dogs.entity.Dog
import de.nilsdruyen.koncept.domain.DataSourceError
import de.nilsdruyen.koncept.domain.annotations.MainDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

internal class DogsRepositoryImpl @Inject constructor(
    private val dogsRemoteDataSource: DogsRemoteDataSource,
    private val dogsCacheDataSource: DogsCacheDataSource,
    @MainDispatcher private val dispatcher: CoroutineDispatcher,
) : DogsRepository {

    override fun getList(): Flow<Either<DataSourceError, List<Dog>>> {
        return flow {
            emit(dogsCacheDataSource.getDogList().first())
            dogsRemoteDataSource.getList().also {
                dogsCacheDataSource.setDogList(it.bind())
            }
            emitAll(dogsCacheDataSource.getDogList())
        }.distinctUntilChanged().flowOn(dispatcher)
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