package de.nilsdruyen.app.config

import com.android.build.gradle.LibraryExtension
import de.nilsdruyen.app.ProjectConfig
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

@Suppress("UnstableApiUsage")
internal fun Project.configureAndroidLibrary() {
    configure<LibraryExtension> {
        compileSdk = ProjectConfig.compileSdkVersion
        defaultConfig {
            targetSdk = ProjectConfig.targetSdkVersion
            minSdk = ProjectConfig.minSdkVersion
        }
        libraryVariants.all {
            generateBuildConfigProvider?.configure { enabled = false }
        }
    }
}