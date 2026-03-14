package de.nilsdruyen.app.config

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

public val modulesWithoutTests: List<String> = listOf("design-system", "base-navigation", "common-ui")

internal fun Project.configure() {
    tasks.withType<JavaCompile>().configureEach {
        sourceCompatibility = JavaVersion.VERSION_21.toString()
        targetCompatibility = JavaVersion.VERSION_21.toString()
    }
    val isEntityModule = name.endsWith("-entity")
    val isUiModule = name.endsWith("-ui")
    val composeCompilerReportEnabled = findProperty("composeCompilerReports") == "true"
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
            progressiveMode.set(true)
            freeCompilerArgs.addAll(
                listOfNotNull(
                    "-opt-in=kotlin.RequiresOptIn",
                    "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi".takeIf { !isEntityModule },
                    "-opt-in=androidx.compose.material.ExperimentalMaterialApi".takeIf { isUiModule },
                ).toMutableList().apply {
                    if (isUiModule && composeCompilerReportEnabled) {
                        addAll(
                            listOf(
                                "-P",
                                "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=${layout.buildDirectory}/compose_compiler",
                                "-P",
                                "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=${layout.buildDirectory}/compose_compiler",
                            )
                        )
                    }
                }
            )
        }
    }

    if (this.name !in modulesWithoutTests) {
        val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
        dependencies {
            val bom = libs.findLibrary("junit5.bom").get()
            add("testImplementation", platform(bom))
            add("testImplementation", libs.findLibrary("junit5.api").get())
            add("testRuntimeOnly", libs.findLibrary("junit5.platform.launcher").get())
            add("testRuntimeOnly", libs.findLibrary("junit5.engine").get())
        }
    }

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
        failFast = true
        failOnNoDiscoveredTests.set(false)
        testLogging {
            events = setOfNotNull(
                org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
                org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
            )
        }
    }
}
