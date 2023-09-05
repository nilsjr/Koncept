@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("de.nilsdruyen.plugin.kotlin")
    id(libs.plugins.google.ksp.get().pluginId)
}

dependencies {
    implementation(libs.bundles.common)

    implementation(projects.common.commonDomain)
    implementation(projects.common.commonRemote)
    implementation(projects.features.dogs.dogsData)
    implementation(projects.features.dogs.dogsEntity)

    implementation(platform(libs.arrow.bom))
    implementation(libs.arrow.core)

    implementation(libs.square.retrofit)
    implementation(libs.square.moshi)
    ksp(libs.square.moshi.codegen)

    implementation(libs.hilt.core)
    ksp(libs.hilt.compiler)

    // testing
    testImplementation(projects.common.commonTest)
    testImplementation(projects.features.dogs.dogsTest)

    testImplementation(libs.bundles.test)
    testImplementation(libs.bundles.mockito)
    testImplementation(libs.mockito.jupiter)

    testImplementation(platform(libs.junit5.bom))
    testImplementation(libs.junit5.api)
    testRuntimeOnly(libs.junit5.engine)

    testImplementation(platform(libs.square.okhttp.bom))
    testImplementation(libs.square.okhttp.mockwebserver)
    testImplementation(libs.square.retrofit)
    testImplementation(libs.square.retrofit.moshi)
    testImplementation(libs.square.moshi)
}