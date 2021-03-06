package de.nilsdruyen.koncept.data

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import de.nilsdruyen.koncept.dogs.cache.daos.DogDao
import de.nilsdruyen.koncept.dogs.cache.entities.DogCacheEntity

@Database(
    entities = [DogCacheEntity::class],
    version = 2,
    autoMigrations = [
        AutoMigration (from = 1, to = 2)
    ]
)
abstract class KonceptDatabase : RoomDatabase() {

    abstract fun dogDao(): DogDao

    companion object {

        private var instance: KonceptDatabase? = null

        @JvmStatic
        @Synchronized
        fun getInstance(applicationContext: Context): KonceptDatabase {
            if (instance == null) {
                instance =
                    Room.databaseBuilder(
                        applicationContext,
                        KonceptDatabase::class.java,
                        "koncept.db"
                    )
//                        .addTypeConverter(StringListConverter(moshi))
                        .fallbackToDestructiveMigration()
                        .build()
            }
            return instance!!
        }
    }
}