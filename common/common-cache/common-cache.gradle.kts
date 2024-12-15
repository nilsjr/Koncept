plugins {
    id("de.nilsdruyen.plugin.kotlin")
    alias(libs.plugins.google.ksp)
}

dependencies {
    implementation(projects.common.commonDomain)

    implementation(libs.kotlinx.coroutines)

    implementation(libs.androidx.datastore.preferences)

    implementation(libs.hilt.core)
    ksp(libs.hilt.compiler)

    implementation(platform(libs.arrow.bom))
    implementation(libs.arrow.core)
}
