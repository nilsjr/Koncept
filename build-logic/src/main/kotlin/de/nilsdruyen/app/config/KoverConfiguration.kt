package de.nilsdruyen.app.config

import kotlinx.kover.KoverPlugin
import kotlinx.kover.api.DefaultJacocoEngine
import kotlinx.kover.api.KoverMergedConfig
import kotlinx.kover.api.KoverProjectConfig
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

internal fun Project.applyKover() {
    pluginManager.apply(KoverPlugin::class)
    configure<KoverProjectConfig> {
        engine.set(DefaultJacocoEngine)
        isDisabled.set(false)
        filters {
            classes {
                includes += listOf("de.nilsdruyen.koncept.*")
                excludes += listOf("*Impl_Factory.*", "*_*Factory", "*_Factory*")
            }
            annotations {
                excludes += listOf("*Generated", "*Composable")
            }
        }
        instrumentation {
            excludeTasks += "testReleaseUnitTest"
        }
    }
}

internal fun Project.applyKoverRoot() {
    pluginManager.apply(KoverPlugin::class)
    configure<KoverMergedConfig> {
        enable()
        filters {
            projects {
                excludes.add("common-test")
                excludes.add("dogs-test")
            }
        }
    }
}