@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("de.nilsdruyen.plugin.kotlin")
    id(libs.plugins.google.ksp.get().pluginId)
}

dependencies {
    implementation(projects.common.commonDomain)

    implementation(libs.kotlinx.coroutines)

    implementation("androidx.datastore:datastore-preferences-core:1.0.0")

    implementation(libs.hilt.core)
    ksp(libs.hilt.compiler)

    implementation(platform(libs.arrow.bom))
    implementation(libs.arrow.core)
}
