package de.nilsdruyen.koncept.dogs.cache

import arrow.core.Either
import de.nilsdruyen.koncept.dogs.cache.daos.DogDao
import de.nilsdruyen.koncept.dogs.cache.entities.DogCacheEntity
import de.nilsdruyen.koncept.dogs.data.DogsCacheDataSource
import de.nilsdruyen.koncept.domain.DataSourceError
import de.nilsdruyen.koncept.domain.toDataSourceError
import de.nilsdruyen.koncept.dogs.entities.Dog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DogsCacheDataSourceImpl @Inject constructor(private val dogDao: DogDao) : DogsCacheDataSource {

    override suspend fun getDogList(): Flow<Either<DataSourceError, List<Dog>>> {
        return dogDao.getAll()
            .map { Either.Right(it.map(DogCacheEntity::toModel)) }
            .catch { throwable ->
                Either.Left(throwable.toDataSourceError())
            }
    }

    override suspend fun setDogList(list: List<Dog>) {
        dogDao.addList(list.map(Dog::toEntity))
    }
}