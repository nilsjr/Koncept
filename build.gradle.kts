import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application") version "7.0.4" apply false
    id("com.android.library") version "7.0.4" apply false
    kotlin("android") version "1.6.10" apply false
    id("dagger.hilt.android.plugin") version "2.40.5" apply false

    id("io.gitlab.arturbosch.detekt") version "1.19.0" apply false
    id("com.github.ben-manes.versions") version "0.41.0" apply false
    id("org.jetbrains.kotlinx.kover") version "0.5.0-RC2"

    id("shot") version "5.12.2" apply false
}

apply(plugin = "io.gitlab.arturbosch.detekt")

subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")

    pluginManager.withPlugin(Plugins.app) {
        extensions.configure<AppExtension> {
            configureAndroidBaseExtension()
        }
    }

    pluginManager.withPlugin(Plugins.library) {
        extensions.configure<LibraryExtension> {
            configureAndroidBaseExtension()
            configureAndroidLibraryExtension()
        }
    }

    tasks.withType<JavaCompile>().configureEach {
        sourceCompatibility = JavaVersion.VERSION_11.toString()
        targetCompatibility = JavaVersion.VERSION_11.toString()
    }

    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_11.toString()
            freeCompilerArgs = freeCompilerArgs + listOf(
                "-progressive", // https://kotlinlang.org/docs/whatsnew13.html#progressive-mode
                "-Xopt-in=kotlin.RequiresOptIn",
                "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-Xopt-in=kotlinx.coroutines.FlowPreview",
                "-Xopt-in=androidx.compose.material.ExperimentalMaterialApi",
            )
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        failFast = true
        testLogging {
            events = setOfNotNull(
                org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
            )
        }
    }

    configureDetekt("src/main/kotlin", "src/test/kotlin")
}

tasks.register<Delete>("clean") {
    delete(buildDir)
}

fun BaseExtension.configureAndroidBaseExtension() {
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }
}

fun LibraryExtension.configureAndroidLibraryExtension() {
    compileSdk = ProjectConfig.compileSdkVersion
    buildToolsVersion = ProjectConfig.buildToolsVersion
    defaultConfig.minSdk = ProjectConfig.minSdkVersion
    libraryVariants.all {
        generateBuildConfigProvider?.configure { enabled = false }
    }
}

fun Project.configureDetekt(vararg paths: String) {
    configure<DetektExtension> {
        toolVersion = "1.19.0"
        source = files(paths)
        parallel = true
        config = files("$rootDir/config/detekt-config.yml")
        buildUponDefaultConfig = true
        ignoreFailures = false
    }
    tasks.withType<Detekt>().configureEach {
        this.jvmTarget = "11"
        reports {
            xml {
                required.set(true)
                outputLocation.set(file("$buildDir/reports/detekt/detekt.xml"))
            }
            html.required.set(false)
            txt.required.set(true)
        }
    }
    dependencies {
        "detektPlugins"("io.gitlab.arturbosch.detekt:detekt-formatting:1.19.0")
    }
}

if (hasProperty("custom")) {
    apply(plugin = "com.github.ben-manes.versions")
    apply(from = "https://raw.githubusercontent.com/JakeWharton/SdkSearch/master/gradle/projectDependencyGraph.gradle")
}