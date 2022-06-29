package de.nilsdruyen.plugin.config

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.file.ConfigurableFileTree
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.tasks.JacocoCoverageVerification
import org.gradle.testing.jacoco.tasks.JacocoReport
import org.gradle.testing.jacoco.tasks.JacocoReportBase

internal fun Project.configureJacoco() {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

    configure<JacocoPluginExtension> {
        toolVersion = libs.findVersion("jacoco").get().toString()
    }

    tasks.withType<JacocoReport> {
        dependsOn(tasks.withType<Test>())

        reports {
            html.required.set(true)
            xml.required.set(true)
        }

        applyDirectories(project)

        doLast {
            println("View code coverage at:")
            println("file://$buildDir/reports/jacoco/test/html/index.html")
        }
    }

    tasks.withType<JacocoCoverageVerification> {
        applyDirectories(project)
        applyRules()
    }
}

object JacocoConfig {

    val minCoverage = "0.38".toBigDecimal()
}

enum class ProjectType {
    APP,
    LIB,
    KOTLIN,
}

fun Project.toType(): ProjectType = when {
    pluginManager.hasPlugin(Constants.Android.Plugin.app) -> ProjectType.APP
    pluginManager.hasPlugin(Constants.Android.Plugin.library) -> ProjectType.LIB
    else -> ProjectType.KOTLIN
}

fun JacocoReportBase.applyAllProjectDirectories(project: Project) {
    with(project) {
        classDirectories.setFrom(files(subprojects.allClassFiles()))
        sourceDirectories.setFrom(files(subprojects.allSourceFiles()))
        executionData.setFrom(files(subprojects.allExecFiles()))
    }
}

fun JacocoReportBase.applyDirectories(project: Project) {
    with(project) {
        classDirectories.setFrom(files(classFiles()))
        sourceDirectories.setFrom(files(sourceFiles()))
        executionData.setFrom(files(execFiles()))
    }
}

fun JacocoCoverageVerification.applyRules() {
    violationRules {
        rule {
            limit {
                minimum = JacocoConfig.minCoverage
            }
        }
    }
}

val ignoreFiles = listOf(
    // android
    "**/R.class",
    "**/R$*.class",
    "**/BuildConfig.*",
    "**/Manifest*.*",
    "**/*Test*.*",
    "android/**/*.*",
    // room
    "**/*_Impl*.*",
    // datastore
    "**/datastore/*.*",
    // kotlin
    "**/*MapperImpl*.*",
    "**/*\$ViewInjector*.*",
    "**/*\$ViewBinder*.*",
    "**/BuildConfig.*",
    "**/*Component*.*",
    "**/*BR*.*",
    "**/Manifest*.*",
    "**/*\$Lambda$*.*",
    "**/*Companion*.*",
    "**/*Module*.*",
    "**/*Dagger*.*",
    "**/*Hilt*.*",
    "**/*MembersInjector*.*",
    "**/*_MembersInjector.class",
    "**/*_Factory*.*",
    "**/*_Provide*Factory*.*",
    "**/*Extensions*.*",
    // sealed and data classes
    "**/*\$Result.*",
    "**/*\$Result$*.*",
    // moshi
    "**/*JsonAdapter.*",
    // hilt
    "**/HiltWrapper_*.*",
    "**/*HiltWrapper_*.*",
    // app specific
    "**/_com_fressnapf_app_*.*",
    "**/FressnapfApp.*",
    // exclude activities, fragment, modules & adapters
    "**/*Activity.*",
    "**/*Fragment.*",
    "**/*Module.*",
    "**/*Adapter.*",
    "**/**/composables/**/*.*",
)

fun Set<Project>.allClassFiles(): List<ConfigurableFileTree> = this.map { it.classFiles() }
fun Set<Project>.allSourceFiles(): List<String> = this.map { it.sourceFiles() }.flatten()
fun Set<Project>.allExecFiles(): List<String> = this.map { it.execFiles() }.filter {
    java.io.File(it).exists()
}

fun Project.classFiles(): ConfigurableFileTree {
    val item = toType().classDirs(buildDir.toString())
    return fileTree(item.buildDir) {
        include(item.includeDirs)
        exclude(ignoreFiles)
    }
}

fun Project.sourceFiles(): List<String> = toType().sourceFiles(projectDir = projectDir.toString())
fun Project.execFiles(): String = toType().execPath(this.buildDir.toString())

data class ProjectClassFiles(
    val buildDir: String,
    val includeDirs: List<String>,
)

fun Project.reportTask(): String {
    return when (this.toType()) {
        ProjectType.APP -> "jacocoDevTestReport"
        ProjectType.LIB -> "jacocoTestReport"
        ProjectType.KOTLIN -> "jacocoTestReport"
    }
}

fun Project.testTask(): String {
    return when (this.toType()) {
        ProjectType.APP -> "testDevDebugUnitTest"
        ProjectType.LIB -> "testDebugUnitTest"
        ProjectType.KOTLIN -> "test"
    }
}

fun ProjectType.classDirs(buildDir: String): ProjectClassFiles {
    val dirs = when (this) {
        ProjectType.APP -> listOf(
            "**/intermediates/javac/devDebug/classes/**",
            "**/tmp/kotlin-classes/devDebug/**",
        )
        ProjectType.LIB -> listOf(
            "**/intermediates/javac/debug/classes/**",
            "**/tmp/kotlin-classes/debug/**",
        )
        ProjectType.KOTLIN -> listOf(
            "**/classes/java/main/**",
            "**/classes/kotlin/main/**",
        )
    }
    return ProjectClassFiles(buildDir, dirs)
}

fun ProjectType.sourceFiles(projectDir: String): List<String> = when (this) {
    ProjectType.APP -> listOf(
        "$projectDir/src/main/kotlin",
        "$projectDir/src/debug/kotlin",
        "$projectDir/src/dev/kotlin",
    )
    ProjectType.LIB -> listOf(
        "$projectDir/src/main/kotlin",
        "$projectDir/src/debug/kotlin",
    )
    ProjectType.KOTLIN -> listOf("$projectDir/src/main/kotlin")
}

fun ProjectType.execPath(buildDir: String): String = when (this) {
    ProjectType.APP -> "$buildDir/jacoco/testDevDebugUnitTest.exec"
    ProjectType.LIB -> "$buildDir/jacoco/testDebugUnitTest.exec"
    ProjectType.KOTLIN -> "$buildDir/jacoco/test.exec"
}
