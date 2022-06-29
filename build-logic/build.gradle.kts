plugins {
    `kotlin-dsl`
    id("io.gitlab.arturbosch.detekt") version "1.21.0-RC1"
}

dependencies {
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.android.gradlePlugin)
    implementation(libs.detekt.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("de.nilsdruyen.plugin.kotlin") {
            id = "de.nilsdruyen.plugin.kotlin"
            implementationClass = "de.nilsdruyen.plugin.KotlinConventionPlugin"
        }
        register("de.nilsdruyen.plugin.android.application") {
            id = "de.nilsdruyen.plugin.android.application"
            implementationClass = "de.nilsdruyen.plugin.ApplicationConventionPlugin"
        }
        register("de.nilsdruyen.plugin.android.library") {
            id = "de.nilsdruyen.plugin.android.library"
            implementationClass = "de.nilsdruyen.plugin.LibraryConventionPlugin"
        }
        register("de.nilsdruyen.plugin.android.library.compose") {
            id = "de.nilsdruyen.plugin.android.library.compose"
            implementationClass = "de.nilsdruyen.plugin.LibraryComposeConventionPlugin"
        }
        register("de.nilsdruyen.plugin.jacoco") {
            id = "de.nilsdruyen.plugin.jacoco"
            implementationClass = "de.nilsdruyen.plugin.JacocoConfigPlugin"
        }
    }
}
