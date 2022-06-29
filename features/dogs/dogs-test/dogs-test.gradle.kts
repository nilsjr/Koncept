plugins {
    id("de.nilsdruyen.plugin.kotlin")
}

dependencies {
    implementation(projects.dogsEntity)

    implementation(libs.faker)
}