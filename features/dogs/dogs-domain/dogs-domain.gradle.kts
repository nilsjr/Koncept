plugins {
    id("kotlin")
}

dependencies {
    implementation(libs.bundles.common)

    implementation(projects.commonDomain)

    implementation(libs.coroutines)
}