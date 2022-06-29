package de.nilsdruyen.plugin

import de.nilsdruyen.plugin.config.configure
import de.nilsdruyen.plugin.config.configureDetekt
import io.gitlab.arturbosch.detekt.DetektPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.PluginManager
import org.gradle.kotlin.dsl.apply

class KotlinConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager){
                apply("kotlin")
                apply(DetektPlugin::class)
            }
            configure()
            configureDetekt("src/main/kotlin", "src/test/kotlin")
        }
    }
}