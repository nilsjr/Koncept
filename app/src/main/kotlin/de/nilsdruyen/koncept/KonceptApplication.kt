package de.nilsdruyen.koncept

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.network.cachecontrol.CacheControlCacheStrategy
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import dagger.hilt.android.HiltAndroidApp
import de.nilsdruyen.koncept.domain.Logger
import de.nilsdruyen.koncept.domain.Logger.Companion.log
import de.nilsdruyen.koncept.utils.DebugTree
import de.nilsdruyen.koncept.utils.LoggerImpl
import timber.log.Timber
import kotlin.time.ExperimentalTime

@HiltAndroidApp
class KonceptApplication : BaseKonceptApplication()

open class BaseKonceptApplication :
    Application(),
    SingletonImageLoader.Factory {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
            Logger.init(LoggerImpl())
        }
    }

    @OptIn(ExperimentalCoilApi::class, ExperimentalTime::class)
    override fun newImageLoader(context: PlatformContext): ImageLoader = ImageLoader.Builder(context)
        .components {
            add(OkHttpNetworkFetcherFactory(cacheStrategy = { CacheControlCacheStrategy() }))
        }
        .logger(object : coil3.util.Logger {
            override var minLevel = coil3.util.Logger.Level.Verbose

            override fun log(tag: String, level: coil3.util.Logger.Level, message: String?, throwable: Throwable?) {
                log(text = message.toString())
            }
        })
        .build()
}
