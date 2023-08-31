@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("de.nilsdruyen.plugin.android.library")
    id(libs.plugins.google.ksp.get().pluginId)
}

android {
    namespace = "de.nilsdruyen.koncept.dogs"
}

dependencies {
    implementation(libs.bundles.common)

    implementation(projects.common.commonDomain)
    implementation(projects.common.commonCache)
    implementation(projects.features.dogs.dogsData)
    implementation(projects.features.dogs.dogsEntity)

    implementation(platform(libs.arrow.bom))
    implementation(libs.arrow.core)

    implementation(libs.hilt.core)
    ksp(libs.hilt.compiler)

    implementation(libs.androidx.room)
    ksp(libs.androidx.room.compiler)

    implementation(libs.androidx.datastore)
}