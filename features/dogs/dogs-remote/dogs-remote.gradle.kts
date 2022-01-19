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

    implementation(libs.arrowKt)
    implementation(libs.retrofit)
    implementation(libs.moshi)
    kapt(libs.moshiCompiler)

    implementation(libs.hiltCore)
    kapt(libs.hiltCompiler)

    testImplementation(projects.commonTest)
    testImplementation(projects.dogsTest)
    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.junitEngine)
}