package de.nilsdruyen.app.plugins

import de.nilsdruyen.app.config.configure
import de.nilsdruyen.app.config.configureDetekt
import io.gitlab.arturbosch.detekt.DetektPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

@Suppress("unused")
internal class KotlinConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("kotlin")
                apply(DetektPlugin::class)
            }
            configure()
            configureDetekt("src/main/kotlin", "src/test/kotlin")
        }
    }
}