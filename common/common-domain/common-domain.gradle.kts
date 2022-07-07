@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("de.nilsdruyen.plugin.kotlin")
}
dependencies {
    implementation(libs.javaxInject)

    implementation(libs.hilt.core)

    implementation(platform(libs.arrow.bom))
    implementation(libs.arrow.core)
}