package de.nilsdruyen.app.config

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType

internal fun Project.configureDetekt(vararg paths: String) {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    val version = libs.findVersion("detekt").get().toString()
    configure<DetektExtension> {
        toolVersion = version
        source = files(paths)
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
        "detektPlugins"("io.gitlab.arturbosch.detekt:detekt-formatting:$version")
    }
}