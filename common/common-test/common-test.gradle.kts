plugins {
    id("kotlin")
}

dependencies {
    implementation(libs.junitApi)
    implementation(libs.coroutinesTest)

    implementation(libs.moshi)
    implementation(libs.moshiKotlin)
}