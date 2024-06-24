@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("de.nilsdruyen.plugin.kotlin")
    id(libs.plugins.google.ksp.get().pluginId)
}

dependencies {
    implementation(projects.common.commonDomain)

    implementation(libs.square.retrofit)
    implementation(libs.square.retrofit.moshi)

    implementation(platform(libs.square.okhttp.bom))
    implementation(libs.square.okhttp)
    implementation(libs.square.okhttp.interceptor)

    implementation(libs.hilt.core)
    ksp(libs.hilt.compiler)

    implementation(platform(libs.arrow.bom))
    implementation(libs.arrow.core)
}
