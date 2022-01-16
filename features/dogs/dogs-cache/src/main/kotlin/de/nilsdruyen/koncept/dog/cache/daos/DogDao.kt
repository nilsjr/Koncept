package de.nilsdruyen.koncept.dog.cache.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import de.nilsdruyen.koncept.dog.cache.entities.DogCacheEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DogDao {

    @Query("SELECT * from dog_table")
    fun getAll(): Flow<List<DogCacheEntity>>

    @Query("SELECT * from dog_table WHERE id=:id")
    fun getDogById(id: String): Flow<DogCacheEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addList(list: List<DogCacheEntity>)
}