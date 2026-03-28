plugins {
    id("de.nilsdruyen.plugin.kotlin")
    id(libs.plugins.google.ksp.get().pluginId)
}
dependencies {
    implementation(libs.bundles.common)

    implementation(projects.common.commonDomain)
    implementation(projects.features.dogs.dogsDomain)
    implementation(projects.features.dogs.dogsEntity)

    implementation(libs.hilt.core)
    ksp(libs.hilt.compiler)

    implementation(libs.kotlinx.coroutines)

    implementation(platform(libs.arrow.bom))
    implementation(libs.arrow.core)

    testImplementation(libs.bundles.test)
    testImplementation(projects.features.dogs.dogsTest)
    testImplementation(projects.common.commonTest)

    testImplementation(libs.bundles.mockito)
    testImplementation(libs.mockito.jupiter)
}
