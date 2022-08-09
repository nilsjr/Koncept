package de.nilsdruyen.koncept

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import de.nilsdruyen.koncept.domain.Logger
import de.nilsdruyen.koncept.utils.DebugTree
import de.nilsdruyen.koncept.utils.LoggerImpl
import timber.log.Timber

@HiltAndroidApp
open class KonceptApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
            Logger.init(LoggerImpl())
        }
    }
}