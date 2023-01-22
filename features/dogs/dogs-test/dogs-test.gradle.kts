plugins {
    id("de.nilsdruyen.plugin.kotlin")
}

dependencies {
    implementation(projects.features.dogs.dogsEntity)

    implementation(libs.faker)
}