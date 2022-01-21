plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

dependencies {
    implementation(libs.bundles.common)

    implementation(projects.commonDomain)
    implementation(projects.dogsData)
    implementation(projects.dogsEntity)

    implementation(platform(libs.arrowStack))
    implementation(libs.arrowKt)

    implementation(libs.hiltCore)
    kapt(libs.hiltCompiler)

    implementation(libs.room)
    kapt(libs.roomCompiler)

    implementation(libs.datastore)
}