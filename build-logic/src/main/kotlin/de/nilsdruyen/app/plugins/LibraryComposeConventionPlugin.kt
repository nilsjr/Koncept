package de.nilsdruyen.app.plugins

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

@Suppress("unused")
internal class LibraryComposeConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")
            configure<LibraryExtension> {
                buildFeatures {
                    compose = true
                }
            }
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            dependencies {
                val bom = libs.findLibrary("compose.bom").get()
                add("implementation", platform(bom))
                add("implementation", libs.findLibrary("androidx.compose.uiToolingPreview").get())
                add("debugImplementation", libs.findLibrary("androidx.compose.uiTooling").get())
            }
        }
    }
}
