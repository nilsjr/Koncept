@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("de.nilsdruyen.plugin.kotlin")
    id(libs.plugins.kotlin.kapt.get().pluginId)
    id(libs.plugins.google.ksp.get().pluginId)
}

dependencies {
    implementation(libs.bundles.common)

    implementation(projects.commonDomain)
    implementation(projects.commonRemote)
    implementation(projects.dogsData)
    implementation(projects.dogsEntity)

    implementation(platform(libs.arrow.bom))
    implementation(libs.arrow.core)

    implementation(libs.square.retrofit)
    implementation(libs.square.moshi)
    ksp(libs.square.moshi.codegen)

    implementation(libs.hilt.core)
    kapt(libs.hilt.compiler)

    // testing
    testImplementation(projects.commonTest)
    testImplementation(projects.dogsTest)

    testImplementation(libs.bundles.test)

    testImplementation(platform(libs.junit5.bom))
    testImplementation(libs.junit5.api)
    testRuntimeOnly(libs.junit5.engine)

    testImplementation(libs.bundles.mockito)
    testImplementation(libs.mockito.jupiter)
}