package de.nilsdruyen.plugin

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import de.nilsdruyen.plugin.config.configureDetekt
import de.nilsdruyen.plugin.config.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class ApplicationConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("io.gitlab.arturbosch.detekt")
            }
            extensions.configure<BaseAppModuleExtension> {
                configureKotlinAndroid(this)
            }
            configureDetekt("src/main/kotlin", "src/test/kotlin")
        }
    }
}