import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

tasks.withType<JavaCompile>().configureEach {
    sourceCompatibility = JavaVersion.VERSION_21.toString()
    targetCompatibility = JavaVersion.VERSION_21.toString()
}
tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
        freeCompilerArgs.set(listOf("-Xexplicit-api=strict"))
    }
}

dependencies {
    implementation(libs.kotlin.plugin)
    implementation(libs.android.plugin)
    implementation(libs.detekt.plugin)
    implementation(libs.gradleVersions.plugin)
    implementation(libs.kotlinx.kover.plugin)
}

gradlePlugin {
    plugins {
        register("de.nilsdruyen.plugin.root") {
            id = "de.nilsdruyen.plugin.root"
            implementationClass = "de.nilsdruyen.app.plugins.ProjectConventionPlugin"
        }
        register("de.nilsdruyen.plugin.kotlin") {
            id = "de.nilsdruyen.plugin.kotlin"
            implementationClass = "de.nilsdruyen.app.plugins.KotlinConventionPlugin"
        }
        register("de.nilsdruyen.plugin.android.application") {
            id = "de.nilsdruyen.plugin.android.application"
            implementationClass = "de.nilsdruyen.app.plugins.ApplicationConventionPlugin"
        }
        register("de.nilsdruyen.plugin.android.library") {
            id = "de.nilsdruyen.plugin.android.library"
            implementationClass = "de.nilsdruyen.app.plugins.LibraryConventionPlugin"
        }
        register("de.nilsdruyen.plugin.android.library.compose") {
            id = "de.nilsdruyen.plugin.android.library.compose"
            implementationClass = "de.nilsdruyen.app.plugins.LibraryComposeConventionPlugin"
        }
        register("de.nilsdruyen.plugin.jacoco") {
            id = "de.nilsdruyen.plugin.jacoco"
            implementationClass = "de.nilsdruyen.app.plugins.JacocoConfigPlugin"
        }
    }
}
