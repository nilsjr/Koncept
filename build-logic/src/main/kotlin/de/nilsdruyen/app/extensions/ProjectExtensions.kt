package de.nilsdruyen.app.extensions

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension

internal val Project.libs
    get(): VersionCatalog = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

private val modulesWithoutTests = listOf(
    "design-system",
    "common-ui",
    "base-navigation",
    "dogs-cache", // TODO: check module configuration
)

internal fun Project.hasTests(): Boolean = !modulesWithoutTests.contains(name)
