import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileTree
import org.gradle.testing.jacoco.tasks.JacocoCoverageVerification
import org.gradle.testing.jacoco.tasks.JacocoReportBase

object JacocoConfig {

    const val toolVersion = "0.8.7"

    val minCoverage = "0.80".toBigDecimal()
}

enum class ProjectType {
    APP,
    LIB,
    KOTLIN,
}

fun Project.toType(): ProjectType = when {
    pluginManager.hasPlugin(Plugins.app) -> ProjectType.APP
    pluginManager.hasPlugin(Plugins.library) -> ProjectType.LIB
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
    "**/_de_nilsdruyen_koncept_*.*",
    // app specific
    "**/KonceptApplication.*",
    "**/components/*.*",
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

fun ProjectType.classDirs(buildDir: String): ProjectClassFiles {
    val dirs = when (this) {
        ProjectType.APP, ProjectType.LIB -> listOf(
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
    )
    ProjectType.LIB -> listOf(
        "$projectDir/src/main/kotlin",
        "$projectDir/src/debug/kotlin",
    )
    ProjectType.KOTLIN -> listOf("$projectDir/src/main/kotlin")
}

fun ProjectType.execPath(buildDir: String): String = when (this) {
    ProjectType.APP -> "$buildDir/jacoco/testDebugUnitTest.exec"
    ProjectType.LIB -> "$buildDir/jacoco/testDebugUnitTest.exec"
    ProjectType.KOTLIN -> "$buildDir/jacoco/test.exec"
}