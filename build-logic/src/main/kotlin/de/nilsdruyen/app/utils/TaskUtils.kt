package de.nilsdruyen.app.utils

import org.gradle.api.Project
import org.gradle.api.Task

internal object TaskUtils {

    fun filterTestTasks(projectList: List<Project>): List<Task> {
        return projectList.map { project ->
            val tasks = mutableListOf<Task>()
            project.pluginManager.withPlugin("com.android.application") {
                tasks.addAll(project.tasks.filter { it.name == "testDebugUnitTest" })
            }
            project.pluginManager.withPlugin("com.android.library") {
                tasks.addAll(project.tasks.filter { it.name == "testDebugUnitTest" })
            }
            if (tasks.isEmpty()) {
                tasks.add(project.tasks.first { it.name == "test" })
            }
            tasks
        }.flatten()
    }
}