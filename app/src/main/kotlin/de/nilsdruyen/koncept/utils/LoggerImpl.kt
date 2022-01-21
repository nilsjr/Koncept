package de.nilsdruyen.koncept.utils

import de.nilsdruyen.koncept.domain.Logger
import timber.log.Timber

class LoggerImpl : Logger {

    override fun log(text: String) {
        Timber.d(text)
    }

    override fun e(message: String) {
        Timber.e(message)
    }
}