plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("shot")
}

android {
    defaultConfig {
        testApplicationId = "de.nilsdruyen.koncept.dogs.test"
        testInstrumentationRunner = "de.nilsdruyen.koncept.dogs.ui.CustomTestRunner"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
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

    implementation(libs.hilt)
    kapt(libs.hiltCompiler)
    implementation(libs.hiltNavigation)

    implementation(libs.viewModelCompose)
    implementation(libs.composeActivity)

    implementation(libs.composeUi)
    implementation(libs.composeFoundation)
    implementation(libs.composeMaterial)
    implementation(libs.composeMaterialIcons)
    implementation(libs.composeMaterial3)
    implementation(libs.composeUiToolingPreview)
    implementation(libs.composeNavigation)
    implementation(libs.constraintCompose)
    implementation(libs.coilCompose)

    debugImplementation(libs.composeUiTooling)

    implementation(platform(libs.arrowStack))
    implementation(libs.arrowKt)

    implementation(libs.lottie)

    // testing
    testImplementation(libs.bundles.test)
    testImplementation(platform(libs.junit5Bom))
    testImplementation(libs.junit5Api)
    testRuntimeOnly(libs.junit5Engine)

    testImplementation(libs.junit4)
    testRuntimeOnly(libs.junit5VintageEngine)

    testImplementation(libs.bundles.mockito)
    testImplementation(libs.mockitoJupiter)

    testImplementation(projects.commonTest)
    testImplementation(projects.commonRemote)

    testImplementation(projects.dogsTest)
    testImplementation(projects.dogsData)
    testImplementation(projects.dogsRemote)
    testImplementation(projects.dogsCache)

    testImplementation(libs.androidxTestCore)
    testImplementation(libs.robolectric)
    testImplementation(libs.hiltTest)
    kaptTest(libs.hiltCompiler)

    // android testing
    androidTestImplementation(libs.bundles.androidTest)
    androidTestImplementation(libs.hiltTest)
    androidTestImplementation(libs.composeUiTest)
    debugImplementation(libs.composeUiManifestTest)
    kaptAndroidTest(libs.hiltCompiler)
    androidTestImplementation(libs.androidxTestRunner)

    androidTestImplementation(projects.dogsTest)
}