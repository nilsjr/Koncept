plugins {
    id("de.nilsdruyen.plugin.kotlin")
}
dependencies {
    implementation(platform(libs.compose.bom))
    implementation(libs.androidx.compose.runtime)
}