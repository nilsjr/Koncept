plugins {
    id("kotlin")
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
}