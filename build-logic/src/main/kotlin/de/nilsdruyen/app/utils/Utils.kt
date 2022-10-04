package de.nilsdruyen.app.utils

import java.io.IOException
import java.util.concurrent.TimeUnit

public fun getEnv(name: String, default: String = ""): String = System.getenv(name) ?: default

internal val isCI = System.getenv("FROM_JENKINS").toBoolean()

internal fun List<String>.runCmd(): String {
    try {
        ProcessBuilder(this).redirectErrorStream(true).start().run {
            waitFor(10, TimeUnit.SECONDS)
            val outputMessage = inputStream.bufferedReader().use { it.readText().trim() }
            if (exitValue() == 0) {
                return outputMessage
            } else {
                throw IOException(outputMessage)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        throw e
    }
}