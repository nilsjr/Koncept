plugins {
    id("de.nilsdruyen.plugin.kotlin")
}

dependencies {
    implementation(libs.junit4)

    implementation(platform(libs.junit5.bom))
    implementation(libs.junit5.api)
    
    implementation(libs.kotlinx.coroutines)
    implementation(libs.kotlinx.coroutines.test)

    implementation(libs.square.moshi)
    implementation(libs.square.moshi.kotlin)
}