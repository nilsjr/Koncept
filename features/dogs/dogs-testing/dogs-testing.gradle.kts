plugins {
    id("de.nilsdruyen.plugin.kotlin")
}
dependencies {
    implementation(projects.common.commonDomain)
    implementation(projects.common.commonTest)
    implementation(projects.features.dogs.dogsEntity)
    implementation(projects.features.dogs.dogsDomain)
    implementation(projects.features.dogs.dogsData)
    implementation(projects.features.dogs.dogsRemote)

    implementation(platform(libs.arrow.bom))
    implementation(libs.arrow.core)

//    implementation(libs.hilt.core)
//    implementation(libs.hilt.android.test)
}