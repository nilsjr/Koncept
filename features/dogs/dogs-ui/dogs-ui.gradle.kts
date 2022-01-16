plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

android {
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    failFast = true
    testLogging {
        events = setOfNotNull(
            org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
        )
    }
}

dependencies {
    implementation(libs.bundles.common)

    implementation(projects.commonDomain)
    implementation(projects.commonUi)

    implementation(projects.dogsDomain)

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

    debugImplementation(libs.composeUiTooling)

    implementation(libs.lottie)

    testImplementation(projects.dogsTest)
    testImplementation(projects.commonTest)
    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.junitEngine)
}