package de.nilsdruyen.app.config

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType

internal fun Project.applyDetekt() {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    pluginManager.apply(DetektPlugin::class)
    configure<DetektExtension> {
        source = files(projectDir)
        parallel = true
        config = files("$rootDir/config/detekt-config.yml")
        buildUponDefaultConfig = true
        ignoreFailures = false
    }
    tasks.withType<Detekt>().configureEach {
        this.jvmTarget = "11"
        reports {
            xml {
                required.set(true)
                outputLocation.set(file("$buildDir/reports/detekt/detekt.xml"))
            }
            html.required.set(false)
            txt.required.set(true)
        }
    }
    dependencies {
        add("detektPlugins", libs.findLibrary("detekt.twitterComposeRules").get())
        add("detektPlugins", libs.findLibrary("detekt.formatting").get())
    }
}

internal fun Project.applyDetektRoot() {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    pluginManager.apply(DetektPlugin::class)
    tasks.register<Detekt>("detektAll") {
        description = "Runs a custom detekt build."
        parallel = true
        buildUponDefaultConfig = true
        jvmTarget = "11"
        setSource(files(projectDir))
        config.setFrom(files("$rootDir/config/detekt-config.yml"))
        include("**/*.kt")
        include("**/*.kts")
        exclude("**/resources/**")
        exclude("**/build/**")
        reports {
            xml {
                required.set(true)
                outputLocation.set(file("$buildDir/reports/detekt/detekt.xml"))
            }
            html.required.set(false)
            txt.required.set(true)
        }
    }
    dependencies {
        add("detektPlugins", libs.findLibrary("detekt.twitterComposeRules").get())
        add("detektPlugins", libs.findLibrary("detekt.formatting").get())
    }
}

internal fun Project.applyDetektFormatting() {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    pluginManager.apply(DetektPlugin::class)
    tasks.register<Detekt>("ktlintCheck") {
        description = "Run detekt ktlint wrapper"
        parallel = true
        setSource(files(projectDir))
        config.setFrom(files("$rootDir/config/detekt-formatting.yml"))
        buildUponDefaultConfig = true
        disableDefaultRuleSets = true
        reports {
            xml {
                required.set(true)
                outputLocation.set(file("$buildDir/reports/detekt/detektFormatting.xml"))
            }
            html.required.set(false)
            txt.required.set(false)
        }
        if (this@applyDetektFormatting == rootProject) {
            include(listOf("*.kts", "build-logic/**/*.kt", "build-logic/**/*.kts"))
            exclude("build-logic/build/")
        } else {
            include(listOf("**/*.kt", "**/*.kts"))
            exclude("build/")
        }
        dependencies {
            add("detektPlugins", libs.findLibrary("detekt.twitterComposeRules").get())
            add("detektPlugins", libs.findLibrary("detekt.formatting").get())
        }
    }
}