@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("de.nilsdruyen.plugin.kotlin")
    id(libs.plugins.kotlin.kapt.get().pluginId)
}

dependencies {
    implementation(projects.commonDomain)

    implementation(libs.hilt.core)
    kapt(libs.hilt.compiler)

    implementation(platform(libs.arrow.bom))
    implementation(libs.arrow.core)
}