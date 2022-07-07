@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.androidGradle) apply false
//    alias(libs.plugins.google.ksp) apply false
    alias(libs.plugins.hilt.android) apply false

    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.ktlint)
    alias(libs.plugins.gradleVersions)

    id("org.jetbrains.kotlinx.kover") version "0.5.1"

    id("shot") version "5.14.1" apply false

    id("de.nilsdruyen.plugin.root")
}

tasks.register<Delete>("clean") {
    delete(buildDir)
}