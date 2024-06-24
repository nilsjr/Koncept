package de.nilsdruyen.koncept

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.test.runner.AndroidJUnitRunner

@Suppress("Unused")
class KonceptRunner : AndroidJUnitRunner() {

    companion object {
        var isRobolectricRun = true
    }

    init {
        System.setProperty("robolectric.invokedynamic.enable", "false")
    }

    override fun onCreate(arguments: Bundle?) {
        super.onCreate(arguments)
        // If the runner is used, this means that Espresso run has been triggered, otherwise is Robolectric JVM run
        isRobolectricRun = false
    }

    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(cl, KonceptTestApplication_Application::class.java.canonicalName, context)
    }
}
