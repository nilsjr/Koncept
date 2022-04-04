import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application") version "7.1.2" apply false
    id("com.android.library") version "7.1.2" apply false
    kotlin("android") version "1.6.10" apply false
    id("com.google.dagger.hilt.android") version "2.41" apply false

    id("io.gitlab.arturbosch.detekt") version "1.19.0" apply false
    id("com.github.ben-manes.versions") version "0.42.0" apply false
    id("org.jetbrains.kotlinx.kover") version "0.5.0"
//    id("org.sonarqube") version "3.3"

    id("shot") version "5.13.0" apply false
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
                org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
            )
        }
    }

    configureDetekt("src/main/kotlin", "src/test/kotlin")
}

tasks.register<Delete>("clean") {
    delete(buildDir)
}

fun BaseExtension.configureAndroidBaseExtension() {
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
        unitTests.all {
            if (it.name == "testDebugUnitTest") {
                it.extensions.configure(kotlinx.kover.api.KoverTaskExtension::class) {
                    isDisabled = false
                    includes = listOf("de.nilsdruyen.koncept.*")
                    excludes = listOf(
                        "hilt_aggregated_deps*",
                        "de.nilsdruyen.koncept.*HiltWrapper*",
                        "de/nilsdruyen/koncept/**/HiltWrapper*.java",
                        "**/HiltWrapper*.*",
                        "*HiltWrapper*.*",
                    )
                }
//        extensions.configure(kotlinx.kover.api.KoverTaskExtension::class) {
//            includes = listOf("de.nilsdruyen.koncept.dogs.domain.usecase.*")
//            excludes = listOf(
//                "de.nilsdruyen.koncept.**Factory",
//                ".+Factory",
//                "hilt_aggregated_deps*",
//                "de.nilsdruyen.koncept.*HiltWrapper*",
//                "de/nilsdruyen/koncept/**/HiltWrapper*.java",
//                "**/HiltWrapper*.*",
//                "*HiltWrapper*.*",
//                "_de_nilsdruyen_koncept_dogs_domain_HiltWrapper_DogsDomainModule.java",
//            )
//        }
            }
        }
    }
    sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
        getByName("test").java.srcDirs("src/test/kotlin")
        getByName("androidTest").java.srcDirs("src/androidTest/kotlin")
    }
}

kover {
    isDisabled = false
    coverageEngine.set(kotlinx.kover.api.CoverageEngine.JACOCO)
//    intellijEngineVersion.set("1.0.647")
    jacocoEngineVersion.set("0.8.7")
    generateReportOnCheck = true
    disabledProjects = setOf("common-test", "dogs-test")
    instrumentAndroidPackage = false
    runAllTestsForProjectTask = false
}

tasks.koverMergedHtmlReport {
    isEnabled = true
    htmlReportDir.set(layout.buildDirectory.dir("kover-report/html-result"))
    includes = listOf("de.nilsdruyen.koncept.*")
    excludes = listOf(
        "",
    )
}

tasks.koverCollectReports {
    outputDir.set(layout.buildDirectory.dir("all-projects-reports"))
}

//tasks.koverMergedVerify {
//    includes = listOf("com.example.*")            // inclusion rules for classes
//    excludes = listOf("com.example.subpackage.*") // exclusion rules for classes
//
//    rule {
//        name = "Minimum number of lines covered"
//        bound {
//            minValue = 100000
//            valueType = kotlinx.kover.api.VerificationValueType.COVERED_LINES_COUNT
//        }
//    }
//    rule {
//        // rule without a custom name
//        bound {
//            minValue = 1
//            maxValue = 1000
//            valueType = kotlinx.kover.api.VerificationValueType.MISSED_LINES_COUNT
//        }
//    }
//    rule {
//        name = "Minimal line coverage rate in percent"
//        bound {
//            minValue = 50
//            // valueType is kotlinx.kover.api.VerificationValueType.COVERED_LINES_PERCENTAGE by default
//        }
//    }
//}
//tasks.koverMergedXmlReport {
//    isEnabled = true                        // false to disable report generation
//    xmlReportFile.set(layout.buildDirectory.file("my-merged-report/result.xml"))
////    includes = listOf("com.example.*")            // inclusion rules for classes
////    excludes = listOf("com.example.subpackage.*") // exclusion rules for classes
//}

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

gradle.projectsEvaluated {
    tasks.register("allTests") {
        dependsOn(TaskUtils.filterTestTasks(subprojects.toList()))
    }
}

if (hasProperty("custom")) {
    apply(plugin = "com.github.ben-manes.versions")
    apply(from = "https://raw.githubusercontent.com/JakeWharton/SdkSearch/master/gradle/projectDependencyGraph.gradle")
}