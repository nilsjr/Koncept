package de.nilsdruyen.app.plugins

import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import de.nilsdruyen.app.config.applyDetektFormatting
import de.nilsdruyen.app.utils.isIgnoredDependency
import de.nilsdruyen.app.utils.releaseType
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.named

@Suppress("unused")
internal class ProjectConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            tasks.named<DependencyUpdatesTask>("dependencyUpdates").configure {
                gradleReleaseChannel = "current"
//                rejectVersionIf {
//                    releaseType(candidate.version) < releaseType(currentVersion) ||
//                        isIgnoredDependency(candidate.group, candidate.module)
//                }
            }
            applyDetektFormatting()
        }
    }
}