package de.nilsdruyen.app.config

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configure() {
    tasks.withType<JavaCompile>().configureEach {
        sourceCompatibility = JavaVersion.VERSION_11.toString()
        targetCompatibility = JavaVersion.VERSION_11.toString()
    }
    val isEntityModule = name.endsWith("-entity")
    val isUiModule = name.endsWith("-ui")
    val composeCompilerReportEnabled = findProperty("composeCompilerReports") == "true"
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_11.toString()
            languageVersion = "1.8"
            freeCompilerArgs = freeCompilerArgs + listOfNotNull(
                "-progressive", // https://kotlinlang.org/docs/whatsnew13.html#progressive-mode
                "-Xcontext-receivers",
                "-opt-in=kotlin.RequiresOptIn",
                "-opt-in=androidx.compose.material.ExperimentalMaterialApi".takeIf { !isEntityModule },
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi".takeIf { !isEntityModule },
            ).toMutableList().apply {
                if (isUiModule && composeCompilerReportEnabled) {
                    addAll(
                        listOf(
                            "-P",
                            "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=${buildDir.absolutePath}/compose_compiler",
                            "-P",
                            "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=${buildDir.absolutePath}/compose_compiler",
                        )
                    )
                }
            }
        }
    }
    tasks.withType<Test> {
        useJUnitPlatform()
        failFast = true
        testLogging {
            events = setOfNotNull(
                org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
                org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
            )
        }
    }
}