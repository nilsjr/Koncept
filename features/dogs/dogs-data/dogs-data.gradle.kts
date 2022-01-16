plugins {
    id("kotlin")
}

dependencies {
    implementation(libs.bundles.common)

    implementation(projects.commonDomain)
    implementation(projects.dogsDomain)

    implementation(libs.coroutines)
    implementation(libs.arrowKt)
}