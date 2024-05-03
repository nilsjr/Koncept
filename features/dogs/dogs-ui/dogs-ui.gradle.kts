plugins {
    id("de.nilsdruyen.plugin.android.library")
    id("de.nilsdruyen.plugin.android.library.compose")
    id(libs.plugins.google.ksp.get().pluginId)
    id(libs.plugins.paparazzi.get().pluginId)
    alias(libs.plugins.compose.compiler.report)
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
    ksp(libs.hilt.compiler)

    implementation(libs.androidx.lifecycle.compose)

    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.androidx.compose.viewmodel)
    implementation(libs.androidx.constraintlayout.compose)

    implementation(libs.coilCompose)
    implementation(libs.accompanist.placeholder)

    implementation(platform(libs.arrow.bom))
    implementation(libs.arrow.core)

    implementation(libs.lottie.compose)

    implementation(libs.androidx.palette.ktx)

    // testing
    testImplementation(projects.common.commonTest)
    testImplementation(projects.common.commonRemote)

    testImplementation(projects.features.dogs.dogsTest)
    testImplementation(projects.features.dogs.dogsData)
    testImplementation(projects.features.dogs.dogsRemote)
    testImplementation(projects.features.dogs.dogsCache)
    testImplementation(projects.features.dogs.dogsTesting)

    testImplementation(libs.bundles.test)
    testImplementation(libs.bundles.mockito)
    testImplementation(libs.mockito.jupiter)
    testImplementation(libs.junit4)

    testImplementation(platform(libs.junit5.bom))
    testImplementation(libs.junit5.api)
    testRuntimeOnly(libs.junit5.engine)
    testRuntimeOnly(libs.junit5.vintage.engine)

    testImplementation(libs.robolectric)

    testImplementation(libs.hilt.android.test)
    kspTest(libs.hilt.android.compiler)

    testImplementation(platform(libs.compose.bom))
    testImplementation(libs.androidx.compose.uiTest)
    testImplementation(libs.androidx.compose.uiManifestTest)
    testImplementation(libs.roborazzi)
    testImplementation(libs.roborazzi.rule)

    testImplementation(libs.square.retrofit)
    testImplementation(platform(libs.square.okhttp.bom))
    testImplementation(libs.square.okhttp)
    testImplementation(libs.square.okhttp.interceptor)
    testImplementation(libs.square.moshi)

    // android testing
    androidTestImplementation(libs.bundles.android.test)
    androidTestImplementation(libs.androidx.test.runner)

    androidTestImplementation(libs.hilt.android.test)
    kspAndroidTest(libs.hilt.compiler)

    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.androidx.compose.uiTest)
    debugImplementation(libs.androidx.compose.uiManifestTest)

    androidTestImplementation(projects.features.dogs.dogsTest)
    androidTestImplementation(libs.paparazzi)
}
