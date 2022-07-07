@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("de.nilsdruyen.plugin.android.library")
    id(libs.plugins.kotlin.kapt.get().pluginId)
}

dependencies {
    implementation(libs.bundles.common)

    implementation(projects.commonDomain)
    implementation(projects.dogsData)
    implementation(projects.dogsEntity)

    implementation(platform(libs.arrow.bom))
    implementation(libs.arrow.core)

    implementation(libs.hilt.core)
    kapt(libs.hilt.compiler)

    implementation(libs.androidx.room)
    kapt(libs.androidx.room.compiler)

    implementation(libs.androidx.datastore)
}