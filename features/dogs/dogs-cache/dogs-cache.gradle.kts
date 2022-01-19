plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

dependencies {
    implementation(projects.commonDomain)
    implementation(projects.dogsData)
    implementation(projects.dogsEntity)

    implementation(libs.bundles.common)
    implementation(libs.hiltCore)
    kapt(libs.hiltCompiler)


    implementation(libs.room)
    kapt(libs.roomCompiler)
}