import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.sonarqube.gradle.SonarQubeExtension

plugins {
    id("com.android.application") version "7.1.2" apply false
    id("com.android.library") version "7.1.2" apply false
    kotlin("android") version "1.6.10" apply false
    id("com.google.dagger.hilt.android") version "2.41" apply false

    id("io.gitlab.arturbosch.detekt") version "1.19.0" apply false
    id("com.github.ben-manes.versions") version "0.42.0" apply false
//    id("org.jetbrains.kotlinx.kover") version "0.5.0-RC2"
    id("org.sonarqube") version "3.3"

    id("shot") version "5.12.2" apply false
}

apply(plugin = "io.gitlab.arturbosch.detekt")
apply(plugin = "jacoco-merged")

subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")
    configureDetekt("src/main/kotlin", "src/test/kotlin")

    apply(plugin = "org.sonarqube")

    pluginManager.withPlugin(Plugins.app) {
        apply(plugin = "jacoco-android-config")
        sonarqube.configureAndroidAppModule(this@subprojects)
        extensions.configure<AppExtension> {
            configureAndroidBaseExtension()
        }
    }

    pluginManager.withPlugin(Plugins.library) {
        apply(plugin = "jacoco-android-config")
        sonarqube.configureAndroidLibraryModule(this@subprojects)
        extensions.configure<LibraryExtension> {
            configureAndroidBaseExtension()
            configureAndroidLibraryExtension()
        }
    }

    val isAndroidProject = File(this.projectDir, "src/main/AndroidManifest.xml").exists()
    if (!isAndroidProject) {
        apply(plugin = "jacoco-config")
        sonarqube.configureKotlinModule(this@subprojects)
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
                TestLogEvent.FAILED,
                TestLogEvent.PASSED,
            )
        }
    }
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
//        unitTests.all {
//            if (it.name == "testDebugUnitTest") {
//                it.extensions.configure(kotlinx.kover.api.KoverTaskExtension::class) {
//                    isDisabled = false
//                    binaryReportFile.set(file("$buildDir/custom/debug-report.bin"))
//                    includes = listOf("com.example.*")
//                    excludes = listOf("com.example.subpackage.*")
//                }
//            }
//        }
    }
    sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
        getByName("test").java.srcDirs("src/test/kotlin")
        getByName("androidTest").java.srcDirs("src/androidTest/kotlin")
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

gradle.projectsEvaluated {
    tasks.register("allTests") {
        dependsOn(TaskUtils.filterTestTasks(subprojects.toList()))
    }
}

if (hasProperty("custom")) {
    apply(plugin = "com.github.ben-manes.versions")
    apply(from = "https://raw.githubusercontent.com/JakeWharton/SdkSearch/master/gradle/projectDependencyGraph.gradle")
}

sonarqube {
    properties {
        property("sonar.projectKey", "koncept")
        property("sonar.organization", "nilsjr")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.login", findStringProperty("konceptSonarToken"))
        property("sonar.sourceEncoding", "UTF-8")
        property("sonar.projectName", "Koncept")
        property("sonar.projectVersion", "0.0.2")

        property("sonar.branch.name", "feature/fix-di")
    }
}

fun Project.findStringProperty(propertyName: String, default: String = ""): String {
    return findProperty(propertyName) as String? ?: run {
        println("$propertyName missing in gradle.properties")
        default
    }
}

fun SonarQubeExtension.configureAndroidAppModule(project: Project) {
    isSkipProject = SonarQubeConfig.skipProject(project)
    properties {
        property("sonar.exclusions", SonarQubeConfig.fileExclusions)
        property("sonar.coverage.exclusions", SonarQubeConfig.appCoverageExclusions)
        property(
            "sonar.kotlin.detekt.reportPaths",
            "${project.buildDir}/reports/detekt/detekt.xml"
        )
//        property(
//            "sonar.androidLint.reportPaths",
//            "${project.rootProject.buildDir}/reports/lint-report.xml"
//        )
        property(
            "sonar.coverage.jacoco.xmlReportPaths",
            "${project.buildDir}/reports/jacoco/jacocoTestReport/jacocoTestReport.xml"
        )
        property("sonar.tests", "src/test/kotlin")
        property(
            "sonar.junit.reportPaths",
            "${project.buildDir}/test-results/testDebugUnitTest"
        )
    }
}

fun SonarQubeExtension.configureAndroidLibraryModule(project: Project) {
    isSkipProject = SonarQubeConfig.skipProject(project)
    properties {
        property("sonar.exclusions", SonarQubeConfig.fileExclusions)
        property("sonar.coverage.exclusions", SonarQubeConfig.libCoverageExclusions)
        property(
            "sonar.kotlin.detekt.reportPaths",
            "${project.buildDir}/reports/detekt/detekt.xml"
        )
//        property(
//            "sonar.androidLint.reportPaths",
//            "${project.rootProject.buildDir}/reports/lint-report.xml"
//        )
        property(
            "sonar.coverage.jacoco.xmlReportPaths",
            "${project.buildDir}/reports/jacoco/jacocoTestReport/jacocoTestReport.xml"
        )
        property("sonar.tests", "src/test/kotlin")
        property(
            "sonar.junit.reportPaths",
            "${project.buildDir}/test-results/testDebugUnitTest"
        )
    }
}

fun SonarQubeExtension.configureKotlinModule(project: Project) {
    isSkipProject = SonarQubeConfig.skipProject(project)
    properties {
        property(
            "sonar.kotlin.detekt.reportPaths",
            "${project.buildDir}/reports/detekt/detekt.xml"
        )
        property(
            "sonar.coverage.jacoco.xmlReportPaths",
            "${project.buildDir}/reports/jacoco/test/jacocoTestReport.xml"
        )
    }
}
