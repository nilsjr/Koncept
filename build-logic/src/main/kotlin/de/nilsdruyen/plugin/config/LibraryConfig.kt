package de.nilsdruyen.plugin.config

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Project

internal fun Project.configureKotlinAndroidLibrary(
    commonExtension: LibraryExtension,
) {
    commonExtension.apply {
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