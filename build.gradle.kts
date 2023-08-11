@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.androidGradle) apply false
    alias(libs.plugins.google.ksp) apply false
    alias(libs.plugins.hilt.android) apply false

    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.gradleVersions)
    alias(libs.plugins.paparazzi) apply false
    alias(libs.plugins.roborazzi) apply false
    alias(libs.plugins.compose.compiler.report) apply false
    alias(libs.plugins.androidTest) apply false
    alias(libs.plugins.androidx.baselineprofile) apply false

    id("de.nilsdruyen.plugin.root")
}

tasks.register<Delete>("clean") {
    delete(layout.buildDirectory)
}