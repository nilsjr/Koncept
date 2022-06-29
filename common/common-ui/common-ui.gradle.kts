plugins {
    id("de.nilsdruyen.plugin.android.library")
    id("de.nilsdruyen.plugin.android.library.compose")
    kotlin("kapt")
}

dependencies {
    implementation(projects.commonDomain)

    implementation(libs.composeUi)
    implementation(libs.composeFoundation)
    implementation(libs.composeMaterial)
    implementation(libs.composeMaterialIcons)
    implementation(libs.composeMaterial3)
    implementation(libs.composeUiToolingPreview)
    implementation(libs.composeNavigation)
    implementation(libs.constraintCompose)

    debugImplementation(libs.composeUiTooling)

    implementation(libs.hilt)
    kapt(libs.hiltCompiler)
}