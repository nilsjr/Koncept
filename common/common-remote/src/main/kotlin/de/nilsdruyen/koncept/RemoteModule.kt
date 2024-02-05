package de.nilsdruyen.koncept

import com.squareup.moshi.Moshi
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.nilsdruyen.koncept.adapters.EitherCallAdapterFactory
import de.nilsdruyen.koncept.annotations.ApiKey
import de.nilsdruyen.koncept.domain.Logger
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    private const val OKHTTP_TIMEOUT_SECONDS = 30L

    @Provides
    fun provideBaseHttpClient(
        cache: Cache,
        loggingInterceptor: HttpLoggingInterceptor,
        @ApiKey headerInterceptor: Interceptor,
        mainThreadCheck: MainThreadCheck,
    ): OkHttpClient {
        if (mainThreadCheck.check()) Logger.e("Initializing OkHttpClient on main thread.")
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
    fun provideBaseRetrofit(client: Lazy<OkHttpClient>, moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.thedogapi.com/v1/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(EitherCallAdapterFactory())
            .callFactory { client.get().newCall(it) }
            .build()
    }
}
