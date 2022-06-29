plugins {
    id("de.nilsdruyen.plugin.kotlin")
}

dependencies {
    implementation(libs.junit4)

    implementation(platform(libs.junit5Bom))
    implementation(libs.junit5Api)
    
    implementation(libs.coroutinesTest)

    implementation(libs.moshi)
    implementation(libs.moshiKotlin)
}