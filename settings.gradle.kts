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
                "app.cash.paparazzi" -> useModule("app.cash.paparazzi:paparazzi-gradle-plugin:${requested.version}")
            }
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://androidx.dev/storage/compose-compiler/repository/")
    }
}

includeBuild("build-logic")

include(":app")

// dogs feature
include(":features:dogs:dogs-domain")
include(":features:dogs:dogs-entity")
include(":features:dogs:dogs-data")
include(":features:dogs:dogs-cache")
include(":features:dogs:dogs-remote")
include(":features:dogs:dogs-ui")
include(":features:dogs:dogs-test")
include(":features:dogs:dogs-testing")

// common modules
include(":common:common-domain")
include(":common:common-remote")
include(":common:common-cache")
include(":common:common-ui")
include(":common:common-test")

// base modules
include(":base:base-navigation")

// design
include(":design:design-system")

// tooling
include(":baselineprofile")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

tailrec fun Collection<ProjectDescriptor>.traverse(action: (ProjectDescriptor) -> Unit) {
    if (isEmpty()) return
    flatMap {
        action(it)
        it.children
    }.traverse(action)
}

rootProject.children.traverse { it.buildFileName = "${it.name}.gradle.kts" }
