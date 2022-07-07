package de.nilsdruyen.app.plugins

import com.android.build.gradle.LibraryExtension
import de.nilsdruyen.app.config.configure
import de.nilsdruyen.app.config.configureAndroidLibrary
import de.nilsdruyen.app.config.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

@Suppress("unused")
internal class LibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
//                apply(DetektPlugin::class)
            }
            configure<LibraryExtension> {
                configureKotlinAndroid()
                configureAndroidLibrary()
            }
            configure()
//            configureDetekt("src/main/kotlin", "src/test/kotlin")
        }
    }
}