package de.nilsdruyen.app.config

import com.android.build.gradle.BaseExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal fun Project.configureKotlinAndroid() {
    configure<BaseExtension> {
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }
        testOptions {
            animationsDisabled = true
            unitTests {
                isIncludeAndroidResources = true
                isReturnDefaultValues = true
            }
        }
        sourceSets {
            getByName("main").java.srcDirs("src/main/kotlin")
            getByName("test").java.srcDirs("src/test/kotlin")
            getByName("androidTest").java.srcDirs("src/androidTest/kotlin")
        }
    }
}