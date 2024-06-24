package de.nilsdruyen.koncept.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import de.nilsdruyen.koncept.BuildConfig
import de.nilsdruyen.koncept.annotations.ApiKey
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.io.File

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    private const val CACHE_SIZE = 25L * 1024L * 1024L
    private const val CURRENT_CACHE_FOLDER = "http_cache"

    @Provides
    fun provideHttpCache(@ApplicationContext context: Context): Cache =
        Cache(File(context.cacheDir, CURRENT_CACHE_FOLDER), CACHE_SIZE)

    @Provides
    @ApiKey
    fun provideHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            chain.run {
                proceed(
                    request()
                        .newBuilder()
                        .addHeader("x-api-key", BuildConfig.DOG_API_KEY)
                        .build()
                )
            }
        }
    }

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor {
            Timber.tag("OkHttp")
            Timber.d(it)
        }.apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }
}
