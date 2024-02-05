@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("de.nilsdruyen.plugin.kotlin")
    id(libs.plugins.google.ksp.get().pluginId)
}
dependencies {
    implementation(libs.javaxInject)

    implementation(libs.kotlinx.coroutines)

    implementation(libs.hilt.core)
    ksp(libs.hilt.compiler)

    implementation(platform(libs.arrow.bom))
    implementation(libs.arrow.core)
}
