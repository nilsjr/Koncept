plugins {
    id("com.android.application") version "7.2.1" apply false
    id("com.android.library") version "7.2.1" apply false
    kotlin("android") version "1.6.21" apply false
    id("com.google.dagger.hilt.android") version "2.42" apply false

    id("io.gitlab.arturbosch.detekt") version "1.21.0-RC1" apply false
    id("com.github.ben-manes.versions") version "0.42.0" apply false
    id("org.jetbrains.kotlinx.kover") version "0.5.1"

    id("shot") version "5.14.1" apply false
}

apply(plugin = "io.gitlab.arturbosch.detekt")

tasks.register<Delete>("clean") {
    delete(buildDir)
}

if (hasProperty("custom")) {
    apply(plugin = "com.github.ben-manes.versions")
    apply(from = "https://raw.githubusercontent.com/JakeWharton/SdkSearch/master/gradle/projectDependencyGraph.gradle")
}