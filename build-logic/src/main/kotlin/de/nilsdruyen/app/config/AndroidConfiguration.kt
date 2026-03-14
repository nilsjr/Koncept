package de.nilsdruyen.app.config

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.invoke

internal fun Project.configureKotlinAndroid() {
    extensions.configure(CommonExtension::class.java) {
        compileOptions.apply {
            sourceCompatibility = JavaVersion.VERSION_21
            targetCompatibility = JavaVersion.VERSION_21
        }
        testOptions.apply {
            animationsDisabled = true
            unitTests {
                isIncludeAndroidResources = true
                isReturnDefaultValues = true
                all {
                    it.maxHeapSize = "1G"
                    it.failOnNoDiscoveredTests.set(false)
                }
            }
        }
        sourceSets {
            getByName("main").java.directories.add("src/main/kotlin")
            getByName("test").java.directories.add("src/test/kotlin")
            getByName("androidTest").java.directories.add("src/androidTest/kotlin")
        }
    }
}
