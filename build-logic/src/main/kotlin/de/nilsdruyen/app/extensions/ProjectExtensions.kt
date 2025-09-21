package de.nilsdruyen.app.extensions

import org.gradle.api.Project

private val modulesWithoutTests = listOf(
    "design-system",
    "common-ui",
    "base-navigation",
    "dogs-cache", // TODO: check module configuration
)

internal fun Project.hasTests(): Boolean = !modulesWithoutTests.contains(name)
