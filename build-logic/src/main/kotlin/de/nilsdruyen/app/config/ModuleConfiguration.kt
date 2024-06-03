package de.nilsdruyen.app.config

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configure() {
    tasks.withType<JavaCompile>().configureEach {
        sourceCompatibility = JavaVersion.VERSION_17.toString()
        targetCompatibility = JavaVersion.VERSION_17.toString()
    }
    val isEntityModule = name.endsWith("-entity")
    val isUiModule = name.endsWith("-ui")
    val composeCompilerReportEnabled = findProperty("composeCompilerReports") == "true"
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
            languageVersion.set(KotlinVersion.KOTLIN_2_0)
            progressiveMode.set(true)
            freeCompilerArgs.addAll(
                listOfNotNull(
                    "-Xcontext-receivers",
                    "-opt-in=kotlin.RequiresOptIn",
                    "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi".takeIf { !isEntityModule },
                    "-opt-in=androidx.compose.material.ExperimentalMaterialApi".takeIf { isUiModule },
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
            )
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
