package de.nilsdruyen.app.utils

private val sortedReleaseQualifiers =
    listOf("preview", "alpha", "beta", "m", "cr", "rc") // order is important!

internal fun releaseType(version: String): Int {
    val index = sortedReleaseQualifiers.indexOfFirst {
        version.matches(".*[.\\-]$it[.\\-\\d]*".toRegex(RegexOption.IGNORE_CASE))
    }
    return if (index < 0) sortedReleaseQualifiers.size else index
}

internal fun isNonStable(version: String): Boolean = releaseType(version) < sortedReleaseQualifiers.size

private val ignoredDependencies = listOf(
    // https://github.com/ben-manes/gradle-versions-plugin/issues/534
    Pair("org.jacoco", "org.jacoco.ant"),
)

internal fun isIgnoredDependency(group: String, module: String) =
    ignoredDependencies.any { it.first == group && it.second == module }
