@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    `kotlin-dsl`
}

tasks.withType<JavaCompile>().configureEach {
    sourceCompatibility = JavaVersion.VERSION_11.toString()
    targetCompatibility = JavaVersion.VERSION_11.toString()
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
        freeCompilerArgs = freeCompilerArgs + "-Xexplicit-api=strict"
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
    @Suppress("DSL_SCOPE_VIOLATION")
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
    }
}