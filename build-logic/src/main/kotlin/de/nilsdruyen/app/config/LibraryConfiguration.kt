package de.nilsdruyen.app.config

import com.android.build.gradle.LibraryExtension
import de.nilsdruyen.app.ProjectConfig
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

@Suppress("UnstableApiUsage")
internal fun Project.configureAndroidLibrary() {
    configure<LibraryExtension> {
        compileSdk = ProjectConfig.compileSdkVersion
        buildToolsVersion = ProjectConfig.buildToolsVersion
        defaultConfig {
            targetSdk = ProjectConfig.targetSdkVersion
        }
        libraryVariants.all {
            generateBuildConfigProvider?.configure { enabled = false }
        }
    }
}