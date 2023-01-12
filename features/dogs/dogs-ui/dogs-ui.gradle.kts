@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("de.nilsdruyen.plugin.android.library")
    id("de.nilsdruyen.plugin.android.library.compose")
    id(libs.plugins.kotlin.kapt.get().pluginId)
    id("shot")
}
android {
    defaultConfig {
        testApplicationId = "de.nilsdruyen.koncept.dogs.test"
        testInstrumentationRunner = "de.nilsdruyen.koncept.dogs.ui.CustomTestRunner"
    }
}
shot {
    applicationId = "de.nilsdruyen.koncept.dogs.test"
}
dependencies {
    implementation(libs.bundles.common)

    implementation(projects.commonDomain)
    implementation(projects.commonUi)
    implementation(projects.baseNavigation)
    implementation(projects.designSystem)

    implementation(projects.dogsDomain)
    implementation(projects.dogsEntity)

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

    testImplementation(projects.commonTest)
    testImplementation(projects.commonRemote)

    testImplementation(projects.dogsTest)
    testImplementation(projects.dogsData)
    testImplementation(projects.dogsRemote)
    testImplementation(projects.dogsCache)

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

    androidTestImplementation(projects.dogsTest)
}