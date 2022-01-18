package de.nilsdruyen.koncept.dogs.data

import arrow.core.Either
import arrow.core.computations.ResultEffect.bind
import de.nilsdruyen.koncept.domain.DataSourceError
import de.nilsdruyen.koncept.domain.repository.DogsRepository
import de.nilsdruyen.koncept.domain.annotations.IoDispatcher
import de.nilsdruyen.koncept.domain.toDataSourceError
import de.nilsdruyen.koncept.entities.Dog
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
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : DogsRepository {

    override suspend fun getList(): Flow<Either<DataSourceError, List<Dog>>> {
        return channelFlow {
            val cache = dogsCacheDataSource.getDogList().flowOn(ioDispatcher)
            val remote = flowOf(dogsRemoteDataSource.getList().also {
                dogsCacheDataSource.setDogList(it.bind())
            }).flowOn(ioDispatcher)
            merge(cache, remote)
                .catch { throwable -> send(Either.Left(throwable.toDataSourceError())) }
                .collect { send(it) }
        }.distinctUntilChanged()
    }
}