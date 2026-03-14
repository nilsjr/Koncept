package de.nilsdruyen.app.config

import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.variant.HasUnitTestBuilder
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import de.nilsdruyen.app.ProjectConfig
import de.nilsdruyen.app.extensions.hasTests
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test

internal fun Project.configureAndroidLibrary() {
    val hasTests = hasTests()
    extensions.configure(LibraryExtension::class.java) {
        compileSdk = ProjectConfig.compileSdkVersion
        defaultConfig {
            minSdk = ProjectConfig.minSdkVersion
        }
        buildFeatures {
            buildConfig = true
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
