package de.nilsdruyen.app.plugins

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import de.nilsdruyen.app.config.configure
import de.nilsdruyen.app.config.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

@Suppress("unused")
internal class ApplicationConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
//                apply("io.gitlab.arturbosch.detekt")
            }
            configure<BaseAppModuleExtension> {
                configureKotlinAndroid()
            }
            configure()
//            configureDetekt("src/main/kotlin", "src/test/kotlin")
        }
    }
}