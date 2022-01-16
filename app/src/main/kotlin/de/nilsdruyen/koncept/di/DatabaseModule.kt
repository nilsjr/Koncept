package de.nilsdruyen.koncept.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.nilsdruyen.koncept.data.KonceptDatabase
import de.nilsdruyen.koncept.dog.cache.daos.DogDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(application: Application): KonceptDatabase =
        KonceptDatabase.getInstance(application)

    @Provides
    @Singleton
    fun KonceptDatabase.provideDogDao(): DogDao = dogDao()
}