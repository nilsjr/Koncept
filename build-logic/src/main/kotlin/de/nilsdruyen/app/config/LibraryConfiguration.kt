package de.nilsdruyen.app.config

import com.android.build.api.variant.HasUnitTestBuilder
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.gradle.LibraryExtension
import de.nilsdruyen.app.ProjectConfig
import de.nilsdruyen.app.extensions.hasTests
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure

internal fun Project.configureAndroidLibrary() {
    val hasTests = hasTests()
    configure<LibraryExtension> {
        compileSdk = ProjectConfig.compileSdkVersion
        defaultConfig {
            minSdk = ProjectConfig.minSdkVersion
        }
        libraryVariants.all {
            generateBuildConfigProvider?.configure { enabled = false }
        }
    }
    extensions.configure(LibraryAndroidComponentsExtension::class.java) {
        beforeVariants { variantBuilder ->
            (variantBuilder as? HasUnitTestBuilder)?.enableUnitTest =
                hasTests && variantBuilder.buildType == "debug"
        }
    }
    if (!hasTests) {
        tasks.withType(Test::class.java).configureEach {
            enabled = false
        }
    }
}
