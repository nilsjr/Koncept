package de.nilsdruyen.app.config

import kotlinx.kover.gradle.plugin.KoverGradlePlugin
import kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension
import kotlinx.kover.gradle.plugin.dsl.KoverReportExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

private val excludeModules = listOf(
    "common-test",
    "dogs-test",
)

internal fun Project.applyKover() {
    pluginManager.apply(KoverGradlePlugin::class)
    configure<KoverProjectExtension> {
        if (excludeModules.contains(this@applyKover.name)) {
            println("kover disabled for ${this@applyKover.name}")
            disable()
        }
        useJacocoTool()
    }
    configure<KoverReportExtension> {
        filters {
            excludes {
                classes("*Impl_Factory.*", "*_*Factory", "*_Factory*")
                annotatedBy("*Generated*", "*Generated", "androidx.compose.runtime.Composable")
            }
//            includes {
//                packages("de.nilsdruyen.koncept.*")
//            }
        }
    }
}

internal fun Project.applyKoverAndroid() {
    pluginManager.apply(KoverGradlePlugin::class)
    configure<KoverProjectExtension> {
        if (excludeModules.contains(this@applyKoverAndroid.name)) disable()
        useJacocoTool()
//        excludeTests {
//            tasks("testReleaseUnitTest")
//        }
    }
    configure<KoverReportExtension> {
        androidReports("debug") {
            filters {
                excludes {
                    classes("*Impl_Factory.*", "*_*Factory", "*_Factory*")
                    annotatedBy("*Generated*", "*Generated", "androidx.compose.runtime.Composable")
                }
//                includes {
//                    packages("de.nilsdruyen.koncept.*")
//                }
            }
        }
    }
}

internal fun Project.applyKoverRoot() {
//    pluginManager.apply(KoverGradlePlugin::class)
//    configure<KoverProjectExtension> {
//        useJacocoTool()
//    }
//    dependencies {
//        add("kover", project(":app"))
//        add("kover", project(":features:dogs:dogs-cache"))
//        add("kover", project(":features:dogs:dogs-remote"))
//        add("kover", project(":features:dogs:dogs-domain"))
//        add("kover", project(":features:dogs:dogs-data"))
//        add("kover", project(":features:dogs:dogs-ui"))
////        subprojects.forEach { subProject ->
////            if (excludeModules.none { subProject.path.contains(it) }) {
////                println("kover add ${subProject.path} - ${subProject.name}")
////                add("kover", subProject.path)
////            }
////        }
//    }
//    configure<KoverReportExtension> {
//        defaults {
//            html {
//                title = "Koncept Kover Report"
//            }
//            verify {
//                rule {
//                    bound {
//                        minValue = 35
//                        maxValue = 75
//                    }
//                    minBound(2)
//                    minBound(98)
//                }
//            }
//        }
//    }
}