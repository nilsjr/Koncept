plugins {
    id("de.nilsdruyen.plugin.kotlin")
    id("de.nilsdruyen.plugin.jacoco")
    kotlin("kapt")
}

dependencies {
    implementation(libs.bundles.common)

    implementation(platform(libs.arrowStack))
    implementation(libs.arrowKt)

    implementation(projects.commonDomain)
    implementation(projects.dogsEntity)

    implementation(libs.hiltCore)
    kapt(libs.hiltCompiler)

    implementation(libs.coroutines)

    testImplementation(libs.bundles.test)
    testImplementation(projects.dogsTest)
    testImplementation(projects.commonTest)

    testImplementation(platform(libs.junit5Bom))
    testImplementation(libs.junit5Api)
    testRuntimeOnly(libs.junit5Engine)

    testImplementation(libs.bundles.mockito)
    testImplementation(libs.mockitoJupiter)
}
