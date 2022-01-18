plugins {
    id("kotlin")
    kotlin("kapt")
}

dependencies {
    implementation(libs.bundles.common)

    implementation(projects.commonDomain)
    implementation(projects.commonRemote)
    implementation(projects.dogsDomain)

    implementation(libs.arrowKt)
    implementation(libs.retrofit)
    implementation(libs.moshi)
    kapt(libs.moshiCompiler)

    testImplementation(projects.commonTest)
    testImplementation(projects.dogsTest)
    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.junitEngine)
}