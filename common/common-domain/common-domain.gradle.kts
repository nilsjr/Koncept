plugins {
    id("kotlin")
}

dependencies {
    implementation(libs.javax)

    implementation(platform(libs.arrowStack))
    implementation(libs.arrowKt)
}