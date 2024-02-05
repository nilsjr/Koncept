package de.nilsdruyen.koncept.data

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.squareup.moshi.Moshi
import de.nilsdruyen.koncept.data.converters.IntRangeConverter
import de.nilsdruyen.koncept.data.converters.StringListConverter
import de.nilsdruyen.koncept.dogs.cache.daos.DogDao
import de.nilsdruyen.koncept.dogs.cache.entities.DogCacheEntity

@Database(
    entities = [DogCacheEntity::class],
    version = 3,
    autoMigrations = [
        AutoMigration(from = 1, to = 3)
    ],
)
@TypeConverters(StringListConverter::class, IntRangeConverter::class)
abstract class KonceptDatabase : RoomDatabase() {

    abstract fun dogDao(): DogDao

    companion object {

        private var instance: KonceptDatabase? = null

        @JvmStatic
        @Synchronized
        fun getInstance(applicationContext: Context, moshi: Moshi): KonceptDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    applicationContext,
                    KonceptDatabase::class.java,
                    "koncept.db"
                )
                    .addTypeConverter(StringListConverter(moshi))
                    .addTypeConverter(IntRangeConverter())
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance!!
        }
    }
}
