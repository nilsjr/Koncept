@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("de.nilsdruyen.plugin.android.library")
    id("de.nilsdruyen.plugin.android.library.compose")
}
android {
    namespace = "de.nilsdruyen.koncept.base.navigation"
}
dependencies {
    implementation(projects.design.designSystem)

    api(libs.androidx.compose.navigation)
    api(libs.hilt.navigation.compose)
    api(libs.accompanist.nav.material)

    api(libs.fornewid.compose.motion.core)
    api(libs.fornewid.compose.motion.navigation)
}
