plugins {
    id("kotlin")
    kotlin("kapt")
}

dependencies {
    implementation(libs.bundles.common)

    implementation(projects.commonDomain)
    implementation(projects.commonRemote)
    implementation(projects.dogsData)
    implementation(projects.dogsEntity)

    implementation(platform(libs.arrowStack))
    implementation(libs.arrowKt)

    implementation(libs.retrofit)
    implementation(libs.moshi)
    kapt(libs.moshiCompiler)

    implementation(libs.hiltCore)
    kapt(libs.hiltCompiler)

    // testing
    testImplementation(projects.commonTest)
    testImplementation(projects.dogsTest)

    testImplementation(libs.bundles.test)

    testImplementation(platform(libs.junit5Bom))
    testImplementation(libs.junit5Api)
    testRuntimeOnly(libs.junit5Engine)

    testImplementation(libs.bundles.mockito)
    testImplementation(libs.mockitoJupiter)
}