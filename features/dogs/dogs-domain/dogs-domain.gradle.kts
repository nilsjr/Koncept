@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("de.nilsdruyen.plugin.kotlin")
    id(libs.plugins.kotlin.kapt.get().pluginId)
}

dependencies {
    implementation(libs.bundles.common)

    implementation(projects.common.commonDomain)
    api(projects.features.dogs.dogsEntity)

    implementation(libs.hilt.core)
    kapt(libs.hilt.compiler)

    implementation(platform(libs.arrow.bom))
    implementation(libs.arrow.core)

    implementation(libs.kotlinx.coroutines)

    testImplementation(libs.bundles.test)
    testImplementation(projects.common.commonTest)
    testImplementation(projects.features.dogs.dogsTest)

    testImplementation(platform(libs.junit5.bom))
    testImplementation(libs.junit5.api)
    testRuntimeOnly(libs.junit5.engine)

    testImplementation(libs.bundles.mockito)
    testImplementation(libs.mockito.jupiter)
}