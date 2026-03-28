package de.nilsdruyen.app.config

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.variant.HasUnitTestBuilder
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import de.nilsdruyen.app.ProjectConfig
import de.nilsdruyen.app.extensions.hasTests
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.invoke

internal fun Project.configureAndroidApplication() {
    extensions.configure(ApplicationExtension::class.java) {
        buildFeatures {
            aidl = false
            resValues = false
            shaders = false
        }
        configureAndroidBase(JavaVersion.VERSION_21)
    }
}

internal fun Project.configureAndroidLibrary() {
    val hasTests = hasTests()
    extensions.configure(LibraryExtension::class.java) {
        compileSdk = ProjectConfig.compileSdkVersion
        defaultConfig {
            minSdk = ProjectConfig.minSdkVersion
        }
        buildFeatures {
            aidl = false
            buildConfig = false
            resValues = false
            shaders = false
        }
        configureAndroidBase(javaVersion = JavaVersion.VERSION_21)
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

private fun CommonExtension.configureAndroidBase(javaVersion: JavaVersion) {
    compileOptions.apply {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }
    testOptions.apply {
        animationsDisabled = true
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
            all {
                it.minHeapSize = "384m"
                it.maxHeapSize = "1G"
            }
        }
    }
    sourceSets {
        getByName("main").java.directories.add("src/main/kotlin")
        getByName("test").java.directories.add("src/test/kotlin")
        getByName("androidTest").java.directories.add("src/androidTest/kotlin")
    }
}
