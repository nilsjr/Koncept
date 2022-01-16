package de.nilsdruyen.koncept.dogs.data

import arrow.core.Either
import arrow.core.computations.ResultEffect.bind
import de.nilsdruyen.koncept.domain.DataSourceError
import de.nilsdruyen.koncept.domain.Dog
import de.nilsdruyen.koncept.domain.DogsCacheDataSource
import de.nilsdruyen.koncept.domain.DogsRemoteDataSource
import de.nilsdruyen.koncept.domain.DogsRepository
import de.nilsdruyen.koncept.domain.annotations.IoDispatcher
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

class DogsRepositoryImpl @Inject constructor(
    val dogsRemoteDataSource: DogsRemoteDataSource,
    val dogsCacheDataSource: DogsCacheDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : DogsRepository {

    override suspend fun getList(): Flow<Either<DataSourceError, List<Dog>>> {
        return channelFlow {
            val cache = dogsCacheDataSource.getDogList()
            val remote = flowOf(dogsRemoteDataSource.getList().also {
                dogsCacheDataSource.setDogList(it.bind())
            })

            merge(cache, remote)
                .catch { throwable ->
                    send(Either.Left(throwable.toDataSourceError()))
                }
                .collect {
                    send(it)
                }
        }.flowOn(ioDispatcher).distinctUntilChanged()
    }
}