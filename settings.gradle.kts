pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                // TODO: remove resolutionStrategy block when https://github.com/google/dagger/issues/2774 is fixed
                "dagger.hilt.android.plugin" -> useModule("com.google.dagger:hilt-android-gradle-plugin:${requested.version}")
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
rootProject.name = "Koncept"

include(":app")

// dogs feature
include(":dogs-domain")
include(":dogs-entities")
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

enableFeaturePreview("VERSION_CATALOGS")
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