@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("de.nilsdruyen.plugin.android.library")
    id("de.nilsdruyen.plugin.android.library.compose")
    id(libs.plugins.kotlin.kapt.get().pluginId)
    id(libs.plugins.paparazzi.get().pluginId)
}
android {
    namespace = "de.nilsdruyen.koncept.dogs.ui"
}
dependencies {
    implementation(libs.bundles.common)

    implementation(projects.common.commonDomain)
    implementation(projects.common.commonUi)
    implementation(projects.base.baseNavigation)
    implementation(projects.design.designSystem)

    implementation(projects.features.dogs.dogsDomain)
    implementation(projects.features.dogs.dogsEntity)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(libs.androidx.lifecycle.compose)

    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.androidx.compose.viewmodel)
    implementation(libs.androidx.constraintlayout.compose)

    implementation(libs.coilCompose)

    implementation(platform(libs.arrow.bom))
    implementation(libs.arrow.core)

    implementation(libs.lottie.compose)

    implementation("androidx.palette:palette-ktx:1.0.0")

    // testing
    testImplementation(libs.bundles.test)
    testImplementation(libs.junit4)

    testImplementation(platform(libs.junit5.bom))
    testImplementation(libs.junit5.api)
    testRuntimeOnly(libs.junit5.engine)
    testRuntimeOnly(libs.junit5.vintage.engine)

    testImplementation(libs.bundles.mockito)
    testImplementation(libs.mockito.jupiter)

    testImplementation(projects.common.commonTest)
    testImplementation(projects.common.commonRemote)

    testImplementation(projects.features.dogs.dogsTest)
    testImplementation(projects.features.dogs.dogsData)
    testImplementation(projects.features.dogs.dogsRemote)
    testImplementation(projects.features.dogs.dogsCache)

    testImplementation(libs.androidx.test.core)
    testImplementation(libs.robolectric)
    testImplementation(libs.hilt.test)
    kaptTest(libs.hilt.compiler)

    testImplementation(libs.square.retrofit)

    testImplementation(platform(libs.square.okhttp.bom))
    testImplementation(libs.square.okhttp)
    testImplementation(libs.square.okhttp.interceptor)
    testImplementation(libs.square.moshi)

    // android testing
    androidTestImplementation(libs.bundles.android.test)
    androidTestImplementation(libs.androidx.test.runner)

    androidTestImplementation(libs.hilt.test)
    kaptAndroidTest(libs.hilt.compiler)

    androidTestImplementation(libs.androidx.compose.uiTest)
    debugImplementation(libs.androidx.compose.uiManifestTest)

    androidTestImplementation(projects.features.dogs.dogsTest)
    androidTestImplementation(libs.paparazzi)
}