package de.nilsdruyen.koncept.di

import android.content.Context
import android.os.Looper
import com.squareup.moshi.Moshi
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import de.nilsdruyen.koncept.BuildConfig
import de.nilsdruyen.koncept.annotations.ApiKey
import de.nilsdruyen.koncept.remote.DogsApi
import de.nilsdruyen.koncept.remote.EitherCallAdapterFactory
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.io.File
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    private const val CACHE_SIZE = 25L * 1024L * 1024L
    private const val OKHTTP_TIMEOUT_SECONDS = 30L
    private const val CURRENT_CACHE_FOLDER = "http_cache"

    @Provides
    fun provideHttpCache(@ApplicationContext context: Context): Cache =
        Cache(File(context.cacheDir, CURRENT_CACHE_FOLDER), CACHE_SIZE)

    @Provides
    fun provideBaseHttpClient(
        cache: Cache,
        loggingInterceptor: HttpLoggingInterceptor,
        @ApiKey headerInterceptor: Interceptor,
    ): OkHttpClient {
        if (Looper.myLooper() == Looper.getMainLooper()) Timber.e("Initializing OkHttpClient on main thread.")
        return OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .addInterceptor(loggingInterceptor)
            .cache(cache)
            .retryOnConnectionFailure(true)
            .connectTimeout(OKHTTP_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(OKHTTP_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(OKHTTP_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
    }

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

    @Provides
    fun provideBaseRetrofit(client: Lazy<OkHttpClient>, moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.thedogapi.com/v1/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(EitherCallAdapterFactory())
            .callFactory { client.get().newCall(it) }
            .build()
    }

    @Provides
    fun Retrofit.provideDogApi() = create(DogsApi::class.java)
}