package de.nilsdruyen.app.plugins

import de.nilsdruyen.app.config.applyDetekt
import de.nilsdruyen.app.config.applyDetektFormatting
import de.nilsdruyen.app.config.configureAndroidApplication
import de.nilsdruyen.app.config.configureModule
import de.nilsdruyen.app.extensions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project

@Suppress("unused")
internal class ApplicationConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.plugin.compose")
            }
            configureAndroidApplication()
            configureModule()

            applyDetekt()
            applyDetektFormatting()

            dependencies.add("coreLibraryDesugaring", libs.findLibrary("android.desugar").get())
        }
    }
}
