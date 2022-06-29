plugins {
    id("de.nilsdruyen.plugin.kotlin")
}
dependencies {
    implementation(libs.javax)

    implementation(libs.hiltCore)

    implementation(platform(libs.arrowStack))
    implementation(libs.arrowKt)
}