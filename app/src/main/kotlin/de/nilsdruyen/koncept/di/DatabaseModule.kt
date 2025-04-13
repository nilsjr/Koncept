package de.nilsdruyen.koncept.di

import android.app.Application
import androidx.room.Room
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.nilsdruyen.koncept.data.KonceptDatabase
import de.nilsdruyen.koncept.data.converters.IntRangeConverter
import de.nilsdruyen.koncept.data.converters.StringListConverter
import de.nilsdruyen.koncept.dogs.cache.daos.DogDao
import de.nilsdruyen.koncept.dogs.cache.daos.DummyDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(application: Application, moshi: Moshi): KonceptDatabase {
        return Room.databaseBuilder(
            application,
            KonceptDatabase::class.java, "koncept"
        )
            .addTypeConverter(IntRangeConverter())
            .addTypeConverter(StringListConverter(moshi))
            .build()
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    @Singleton
    fun provideDogDao(database: KonceptDatabase): DogDao = database.dogDao()

    @Provides
    @Singleton
    fun provideDummyDao(database: KonceptDatabase): DummyDao = database.dummyDao()
}
