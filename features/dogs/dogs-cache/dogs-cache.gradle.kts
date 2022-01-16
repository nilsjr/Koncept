plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

dependencies {
    implementation(projects.commonDomain)
    implementation(projects.dogsDomain)

    implementation(libs.bundles.common)

    implementation(libs.room)
    kapt(libs.roomCompiler)
}