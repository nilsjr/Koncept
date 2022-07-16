package de.nilsdruyen.koncept.dogs.cache.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import de.nilsdruyen.koncept.dogs.cache.entities.DogCacheEntity
import de.nilsdruyen.koncept.dogs.cache.entities.MinimalDogCacheEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DogDao {

    @Query("SELECT * from dog_table")
    fun getAll(): Flow<List<DogCacheEntity>>

    @Query("SELECT * from dog_table WHERE id=:id")
    fun getDogById(id: Int): Flow<DogCacheEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addList(list: List<DogCacheEntity>): List<Long>

    @Insert(entity = DogCacheEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMinimalList(list: List<MinimalDogCacheEntity>): List<Long>

    @Update(entity = DogCacheEntity::class)
    suspend fun updateList(list: List<MinimalDogCacheEntity>)

    @Query("SELECT * from dog_table WHERE isFavorite = 1")
    fun getAllFavorites(): Flow<List<DogCacheEntity>>

    @Query("UPDATE dog_table SET isFavorite = 1 WHERE id = :breedId")
    suspend fun setFavorite(breedId: Int)

    @Query("UPDATE dog_table SET isFavorite = 0 WHERE id = :breedId")
    suspend fun removeFavorite(breedId: Int)

    @Transaction
    suspend fun upsertList(list: List<MinimalDogCacheEntity>) {
        val results = addMinimalList(list)
        val updateList = results.mapIndexedNotNull { index, result ->
            if (result == -1L) {
                list[index]
            } else null
        }
        updateList(updateList)
    }
}