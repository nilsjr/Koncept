package de.nilsdruyen.koncept.dogs.cache

import arrow.core.Either
import de.nilsdruyen.koncept.dogs.cache.daos.DogDao
import de.nilsdruyen.koncept.dogs.cache.entities.DogCacheEntity
import de.nilsdruyen.koncept.dogs.data.DogsCacheDataSource
import de.nilsdruyen.koncept.dogs.entity.Dog
import de.nilsdruyen.koncept.domain.DataSourceError
import de.nilsdruyen.koncept.domain.annotations.IoDispatcher
import de.nilsdruyen.koncept.domain.toDataSourceError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DogsCacheDataSourceImpl @Inject constructor(
    private val dogDao: DogDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : DogsCacheDataSource {

    override suspend fun getDogList(): Flow<Either<DataSourceError, List<Dog>>> {
        return dogDao.getAll()
            .map { Either.Right(it.map(DogCacheEntity::toModel)) }
            .catch { throwable ->
                Either.Left(throwable.toDataSourceError())
            }.flowOn(ioDispatcher)
    }

    override suspend fun setDogList(list: List<Dog>) = withContext(ioDispatcher) {
        dogDao.addList(list.map(Dog::toEntity))
    }
}