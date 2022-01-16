plugins {
    id("kotlin")
}

dependencies {
    implementation(projects.dogsDomain)

    implementation(libs.faker)
}