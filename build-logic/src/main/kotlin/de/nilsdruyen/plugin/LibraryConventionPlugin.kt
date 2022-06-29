package de.nilsdruyen.plugin

import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import de.nilsdruyen.plugin.config.configure
import de.nilsdruyen.plugin.config.configureDetekt
import de.nilsdruyen.plugin.config.configureKotlinAndroid
import de.nilsdruyen.plugin.config.configureKotlinAndroidLibrary
import io.gitlab.arturbosch.detekt.DetektPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

class LibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply(DetektPlugin::class)
            }
            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                configureKotlinAndroidLibrary(this)
            }
            configure()
            configureDetekt("src/main/kotlin", "src/test/kotlin")
        }
    }
}