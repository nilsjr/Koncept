package de.nilsdruyen.plugin

import com.android.build.gradle.LibraryExtension
import io.gitlab.arturbosch.detekt.DetektPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType

class LibraryComposeConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.library")
            with(pluginManager) {
                apply(DetektPlugin::class)
            }
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            extensions.configure<LibraryExtension> {
                buildFeatures {
                    compose = true
                }
                composeOptions {
                    kotlinCompilerExtensionVersion = libs.findVersion("composeCompiler").get().toString()
                }
            }
        }
    }
}