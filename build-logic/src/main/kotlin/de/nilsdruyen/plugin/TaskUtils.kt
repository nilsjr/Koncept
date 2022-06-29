package de.nilsdruyen.plugin

import de.nilsdruyen.plugin.config.Constants
import org.gradle.api.Project
import org.gradle.api.Task

object TaskUtils {

    fun filterTestTasks(projectList: List<Project>): List<Task> {
        return projectList.map { project ->
            val tasks = mutableListOf<Task>()
            project.pluginManager.withPlugin(Constants.Android.Plugin.app) {
                tasks.addAll(project.tasks.filter { it.name == "testDebugUnitTest" })
            }
            project.pluginManager.withPlugin(Constants.Android.Plugin.library) {
                tasks.addAll(project.tasks.filter { it.name == "testDebugUnitTest" })
            }
            if (tasks.isEmpty()) {
                tasks.add(project.tasks.first { it.name == "test" })
            }
            tasks
        }.flatten()
    }
}