package de.nilsdruyen.koncept.data

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import de.nilsdruyen.koncept.data.converters.IntRangeConverter
import de.nilsdruyen.koncept.data.converters.StringListConverter
import de.nilsdruyen.koncept.dogs.cache.daos.DogDao
import de.nilsdruyen.koncept.dogs.cache.daos.DummyDao
import de.nilsdruyen.koncept.dogs.cache.entities.BreedCacheEntity

@Database(
    entities = [BreedCacheEntity::class],
    version = 4,
    autoMigrations = [
        AutoMigration(from = 1, to = 3)
    ],
)
@TypeConverters(IntRangeConverter::class, StringListConverter::class)
abstract class KonceptDatabase : RoomDatabase() {

    abstract fun dogDao(): DogDao
    abstract fun dummyDao(): DummyDao
}
