plugins {
    id("kotlin")
    kotlin("kapt")
}

dependencies {
    implementation(libs.bundles.common)

    implementation(projects.commonDomain)
    implementation(projects.dogsEntity)

    implementation(libs.hiltCore)
    kapt(libs.hiltCompiler)

    implementation(libs.coroutines)
}