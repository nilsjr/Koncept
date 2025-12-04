package de.nilsdruyen.koncept.di

import android.content.Context
import android.os.Looper
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.components.SingletonComponent
import de.nilsdruyen.koncept.MainThreadCheck
import de.nilsdruyen.koncept.base.navigation.Navigator
import de.nilsdruyen.koncept.dogs.ui.navigation.DogListRoute
import javax.inject.Singleton

// At the top level of your kotlin file:
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "koncept_prefs")

@Module
@InstallIn(SingletonComponent::class)
object AppStaticModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder().build()

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> = context.dataStore

    @Provides
    fun mainThreadCheck() = MainThreadCheck { Looper.myLooper() == Looper.getMainLooper() }
}

@Module
@InstallIn(ActivityRetainedComponent::class)
object AppModule {

    @Provides
    @ActivityRetainedScoped
    fun provideNavigator(): Navigator = Navigator(startDestination = DogListRoute)
}