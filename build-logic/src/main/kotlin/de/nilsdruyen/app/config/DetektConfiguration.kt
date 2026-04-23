package de.nilsdruyen.app.config

import dev.detekt.gradle.Detekt
import dev.detekt.gradle.extensions.DetektExtension
import dev.detekt.gradle.plugin.DetektPlugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register

internal fun Project.applyDetekt() {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    pluginManager.apply(DetektPlugin::class.java)
    extensions.configure<DetektExtension> {
        toolVersion.set(libs.findVersion("detekt").get().toString())
        ignoreFailures.set(false)
        parallel.set(true)
        source.setFrom(files("src/main/kotlin"))
        config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
        buildUponDefaultConfig.set(true)
    }
}

internal fun Project.applyDetektFormatting() {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    pluginManager.apply(DetektPlugin::class)

    fun Detekt.configure(enableAutoCorrect: Boolean) {
        description = "Run detekt ktlint wrapper"
        parallel.set(true)
        setSource(layout.projectDirectory)
        config.setFrom(files("$rootDir/config/detekt/detekt-formatting.yml"))
        buildUponDefaultConfig.set(true)
        disableDefaultRuleSets.set(true)
        autoCorrect.set(enableAutoCorrect)
        include("*.kts", "src/*/kotlin/**/*.kt")
        dependencies.add("detektPlugins", libs.findLibrary("detekt.composeRules").get())
        dependencies.add("detektPlugins", libs.findLibrary("detekt.ktlintWrapper").get())
    }

    tasks.register<Detekt>("ktlintCheck") {
        configure(false)
    }
    tasks.register<Detekt>("ktlintFormat") {
        configure(true)
    }
}
