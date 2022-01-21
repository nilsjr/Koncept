plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("shot")
}

android {
    defaultConfig {
        testApplicationId = "de.nilsdruyen.koncept.dogs.test"
        testInstrumentationRunner = "com.karumi.shot.ShotTestRunner"
//        testInstrumentationRunner = "de.nilsdruyen.koncept.dogs.ui.CustomTestRunner"
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
    debugImplementation(projects.dogsTest)

    implementation(libs.lottie)

    testImplementation(projects.dogsTest)
    testImplementation(projects.commonTest)
    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.junitEngine)
    testImplementation(libs.hiltTest)
    kaptTest(libs.hiltCompiler)
//    testImplementation(libs.okHttpMock)

    androidTestImplementation(libs.hiltTest)
    androidTestImplementation(libs.composeUiTest)
    debugImplementation(libs.composeUiManifestTest)
    kaptAndroidTest(libs.hiltCompiler)
    androidTestImplementation("androidx.test:runner:1.4.0")

    androidTestImplementation(projects.dogsData)
    androidTestImplementation(projects.dogsRemote)
    androidTestImplementation(projects.dogsCache)
}