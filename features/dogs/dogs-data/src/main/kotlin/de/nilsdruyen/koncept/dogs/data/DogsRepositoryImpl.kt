package de.nilsdruyen.koncept.dogs.data

import arrow.core.Either
import arrow.core.computations.ResultEffect.bind
import de.nilsdruyen.koncept.dogs.domain.BreedImages
import de.nilsdruyen.koncept.dogs.domain.repository.DogsRepository
import de.nilsdruyen.koncept.dogs.entity.Dog
import de.nilsdruyen.koncept.domain.DataSourceError
import de.nilsdruyen.koncept.domain.toDataSourceError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.merge
import javax.inject.Inject

internal class DogsRepositoryImpl @Inject constructor(
    private val dogsRemoteDataSource: DogsRemoteDataSource,
    private val dogsCacheDataSource: DogsCacheDataSource,
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
        }.distinctUntilChanged()
    }

    override suspend fun getImagesForBreed(breedId: Int): BreedImages {
        return dogsRemoteDataSource.getImagesForBreed(breedId)
    }
}