package de.nilsdruyen.koncept.dogs.cache

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import arrow.core.Either
import de.nilsdruyen.koncept.dogs.cache.daos.DogDao
import de.nilsdruyen.koncept.dogs.cache.entities.DogCacheEntity
import de.nilsdruyen.koncept.dogs.data.DogsCacheDataSource
import de.nilsdruyen.koncept.dogs.entity.Dog
import de.nilsdruyen.koncept.domain.DataSourceError
import de.nilsdruyen.koncept.domain.Logger
import de.nilsdruyen.koncept.domain.annotations.IoDispatcher
import de.nilsdruyen.koncept.domain.toDataSourceError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DogsCacheDataSourceImpl @Inject constructor(
    private val dogDao: DogDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val dataStore: DataStore<Preferences>,
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

    override suspend fun saveListPosition(index: Int, offset: Int) {
        withContext(ioDispatcher) {
            dataStore.edit { settings ->
                settings[intPreferencesKey("list_index")] = index
                settings[intPreferencesKey("list_offset")] = index
            }
        }
    }

    override suspend fun getListPosition(): Pair<Int, Int> {
        return dataStore.data.map { preferences ->
            val index = preferences[intPreferencesKey("list_index")] ?: 0
            val offset = preferences[intPreferencesKey("list_offset")] ?: 0
            Logger.log("get position $index/$offset")
            index to offset
        }.first()
    }
}
