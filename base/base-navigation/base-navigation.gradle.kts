import com.android.build.api.dsl.LibraryExtension

plugins {
    id("de.nilsdruyen.plugin.android.library")
    id("de.nilsdruyen.plugin.android.library.compose")
}
configure<LibraryExtension> {
    namespace = "de.nilsdruyen.koncept.base.navigation"
}
dependencies {
    implementation(projects.design.designSystem)

    api(libs.hilt.lifecycle.viewmodel.compose)

    api(libs.androidx.navigation3.runtime)
    api(libs.androidx.navigation3.ui)
}
