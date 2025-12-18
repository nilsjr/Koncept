plugins {
    id("de.nilsdruyen.plugin.android.library")
    id("de.nilsdruyen.plugin.android.library.compose")
}
android {
    namespace = "de.nilsdruyen.koncept.base.navigation"
}
dependencies {
    implementation(projects.design.designSystem)

    api(libs.hilt.navigation.compose)

    api(libs.androidx.navigation3.runtime)
    api(libs.androidx.navigation3.ui)
}
