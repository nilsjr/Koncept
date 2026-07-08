plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.androidTest) apply false

    alias(libs.plugins.kotlin.androidGradle) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false

    alias(libs.plugins.google.ksp) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.androidx.baselineprofile) apply false

    alias(libs.plugins.detekt)
    alias(libs.plugins.gradleVersions)
    alias(libs.plugins.compose.compiler.report) apply false

    id("de.nilsdruyen.plugin.root")
}

// Hilt's bundled kotlin-metadata-jvm only supports up to 2.3.0; force the version
// that matches the Kotlin compiler so KAPT annotation processing doesn't fail.
allprojects {
    configurations.all {
        resolutionStrategy {
            force("org.jetbrains.kotlin:kotlin-metadata-jvm:${libs.versions.kotlin.get()}")
        }
    }
}

tasks.register<Delete>("clean") {
    delete(layout.buildDirectory)
}
