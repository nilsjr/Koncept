@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("de.nilsdruyen.plugin.kotlin")
    id(libs.plugins.kotlin.kapt.get().pluginId)
}

dependencies {
    implementation(projects.common.commonDomain)

    implementation(libs.kotlinx.coroutines)

    implementation("androidx.datastore:datastore-preferences-core:1.0.0")

    implementation(libs.hilt.core)
    kapt(libs.hilt.compiler)

    implementation(platform(libs.arrow.bom))
    implementation(libs.arrow.core)
}