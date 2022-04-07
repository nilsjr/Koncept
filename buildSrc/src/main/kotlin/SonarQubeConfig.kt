import org.gradle.api.Project

object SonarQubeConfig {

    private const val basePackageName = "de/nilsdruyen/koncept"
    private val libCoverageExclusionsList = listOf(
        "**/$basePackageName/**/*Activity.kt",
        "**/$basePackageName/**/*Module.kt",
        "**/$basePackageName/**/*Adapter.kt",
        "**/$basePackageName/**/*Call.kt",
        "**/$basePackageName/**/components/*.kt",
//    "**/$basePackageName/**/*Fragment.kt",
    )
    val fileExclusions = listOf(
        "**/assets/**/*",
    ).joinToString(separator = ",")
    private val appCoverageExclusionsList = libCoverageExclusionsList + listOf(
        "**/$basePackageName/**/KonceptApplication.kt",
    )

    val appCoverageExclusions = appCoverageExclusionsList.joinToString(separator = ",")
    val libCoverageExclusions = libCoverageExclusionsList.joinToString(separator = ",")

    private val ignoreModules = listOf("common-ui")

    fun skipProject(project: Project): Boolean {
        return ignoreModules.contains(project.name) || project.name.contains("-test")
    }
}

