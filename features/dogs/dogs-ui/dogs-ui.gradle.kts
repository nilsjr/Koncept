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

    implementation(projects.dogsDomain)
    implementation(projects.dogsEntity)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(libs.androidx.compose.viewmodel)
    implementation(libs.androidx.compose.activity)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material.icons)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.navigation)
    implementation(libs.androidx.compose.constraint)
    implementation(libs.coilCompose)

    implementation(libs.androidx.compose.uiToolingPreview)
    debugImplementation(libs.androidx.compose.uiTooling)

    implementation(platform(libs.arrow.bom))
    implementation(libs.arrow.core)

    implementation(libs.lottie.compose)

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

    // android testing
    androidTestImplementation(libs.bundles.android.test)
    androidTestImplementation(libs.hilt.test)
    androidTestImplementation(libs.androidx.compose.uiTest)
    debugImplementation(libs.androidx.compose.uiManifestTest)
    kaptAndroidTest(libs.hilt.compiler)
    androidTestImplementation(libs.androidx.test.runner)

    androidTestImplementation(projects.dogsTest)
}