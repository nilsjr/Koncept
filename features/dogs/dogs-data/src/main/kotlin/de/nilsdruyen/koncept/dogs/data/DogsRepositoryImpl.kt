package de.nilsdruyen.koncept.dogs.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import arrow.core.Either
import arrow.core.computations.ResultEffect.bind
import de.nilsdruyen.koncept.dogs.domain.BreedImages
import de.nilsdruyen.koncept.dogs.domain.repository.DogsRepository
import de.nilsdruyen.koncept.dogs.entity.Breed
import de.nilsdruyen.koncept.dogs.entity.Dog
import de.nilsdruyen.koncept.domain.DataSourceError
import de.nilsdruyen.koncept.domain.annotations.MainDispatcher
import de.nilsdruyen.koncept.domain.toDataSourceError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.merge
import javax.inject.Inject

internal class DogsRepositoryImpl @Inject constructor(
    private val dogsRemoteDataSource: DogsRemoteDataSource,
    private val dogsCacheDataSource: DogsCacheDataSource,
    @MainDispatcher private val dispatcher: CoroutineDispatcher,
) : DogsRepository {

    override suspend fun getList(): Flow<Either<DataSourceError, List<Dog>>> {
        return channelFlow {
            val cache = dogsCacheDataSource.getDogList()
            val remote = flowOf(dogsRemoteDataSource.getList().also {
                dogsCacheDataSource.setDogList(it.bind())
            })
            merge(cache, remote)
                .catch { throwable -> send(Either.Left(throwable.toDataSourceError())) }
                .collect { send(it) }
        }.distinctUntilChanged().flowOn(dispatcher)
    }

    override suspend fun getImagesForBreed(breedId: Int): BreedImages {
        return dogsRemoteDataSource.getImagesForBreed(breedId)
    }

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getBreeds(): Flow<PagingData<Breed>> {
        return Pager(
            config = PagingConfig(pageSize = 5, prefetchDistance = 2),
            remoteMediator = BreedRemoteMediator(dogsRemoteDataSource, dogsCacheDataSource),
            pagingSourceFactory = dogsCacheDataSource::getBreedPaging
        ).flow.flowOn(dispatcher)
    }
}

@OptIn(ExperimentalPagingApi::class)
class BreedRemoteMediator constructor(
    dogsRemoteDataSource: DogsRemoteDataSource,
    dogsCacheDataSource: DogsCacheDataSource
) : RemoteMediator<Int, Breed>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Breed>): MediatorResult {
        TODO("Not yet implemented")
    }
}