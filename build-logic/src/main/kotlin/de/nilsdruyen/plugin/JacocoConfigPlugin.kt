package de.nilsdruyen.plugin

import de.nilsdruyen.plugin.config.configureJacoco
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

class JacocoConfigPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "jacoco")
            configureJacoco()
        }
    }
}
