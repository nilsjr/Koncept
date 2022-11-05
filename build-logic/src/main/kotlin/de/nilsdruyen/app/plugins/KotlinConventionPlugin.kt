package de.nilsdruyen.app.plugins

import de.nilsdruyen.app.config.applyDetekt
import de.nilsdruyen.app.config.applyDetektFormatting
import de.nilsdruyen.app.config.configure
import org.gradle.api.Plugin
import org.gradle.api.Project

@Suppress("unused")
internal class KotlinConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("kotlin")
            }
            configure()
            applyDetekt()
            applyDetektFormatting()
        }
    }
}