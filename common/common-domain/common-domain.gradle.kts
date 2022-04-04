plugins {
    id("kotlin")
}

dependencies {
    implementation(libs.javax)

    implementation(libs.hiltCore)

    implementation(platform(libs.arrowStack))
    implementation(libs.arrowKt)
}