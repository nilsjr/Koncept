import com.android.build.api.dsl.LibraryExtension

plugins {
    id("de.nilsdruyen.plugin.android.library")
    id("de.nilsdruyen.plugin.android.library.compose")
    id(libs.plugins.google.ksp.get().pluginId)
}
configure<LibraryExtension> {
    namespace = "de.nilsdruyen.koncept.common.ui"
}
dependencies {
    implementation(projects.common.commonDomain)

    implementation(platform(libs.compose.bom))
    implementation(libs.androidx.compose.foundation)
    api(libs.androidx.compose.material.icons)
    api(libs.androidx.compose.material3)
    implementation(libs.androidx.constraintlayout.compose)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}
