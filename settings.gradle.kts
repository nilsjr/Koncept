rootProject.name = "koncept"

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "shot" -> useModule("com.karumi:shot:${requested.version}")
            }
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

includeBuild("build-logic")

include(":app")

// dogs feature
include(":dogs-domain")
include(":dogs-entity")
include(":dogs-data")
include(":dogs-cache")
include(":dogs-remote")
include(":dogs-ui")
include(":dogs-test")

// common modules
include(":common-domain")
include(":common-remote")
include(":common-ui")
include(":common-test")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

val skipConfiguration = listOf("app", "buildSrc")
rootProject.children.forEach {
    it.buildFileName = "${it.name}.gradle.kts"
    if (skipConfiguration.contains(it.name)) return@forEach
    val feature = it.name.split("-").first()
    if (it.name.contains("common")) {
        it.projectDir = File(rootDir, "common/${it.name}")
    } else {
        it.projectDir = File(rootDir, "features/$feature/${it.name}")
    }
}