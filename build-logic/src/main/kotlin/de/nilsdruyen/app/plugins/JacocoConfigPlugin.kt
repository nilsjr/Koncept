package de.nilsdruyen.app.plugins

import de.nilsdruyen.app.config.configureJacoco
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

@Suppress("unused")
internal class JacocoConfigPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "jacoco")
            configureJacoco()
        }
    }
}