import com.android.build.api.dsl.ManagedVirtualDevice
import de.nilsdruyen.app.ProjectConfig
import de.nilsdruyen.app.utils.CiUtils

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("de.nilsdruyen.plugin.android.application")
    id(libs.plugins.google.ksp.get().pluginId)
    id(libs.plugins.hilt.android.get().pluginId)
    alias(libs.plugins.androidx.baselineprofile)
}
android {
    namespace = "de.nilsdruyen.koncept"
    compileSdk = ProjectConfig.compileSdkVersion
    defaultConfig {
        applicationId = "de.nilsdruyen.koncept"

        minSdk = ProjectConfig.minSdkVersion
        targetSdk = ProjectConfig.targetSdkVersion
        versionCode = 1
        versionName = "0.0.1"

        testApplicationId = "de.nilsdruyen.koncept.test"
        testInstrumentationRunner = "de.nilsdruyen.koncept.KonceptRunner"
        testInstrumentationRunnerArguments += mapOf("clearPackageData" to "true")

        buildConfigField(
            "String",
            "DOG_API_KEY",
            "\"${findStringProperty("dogApiKey", "DOG_API_KEY")}\""
        )
    }
    signingConfigs {
        getByName("debug") {
            storeFile = file("../debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
        if (findProperty("enableReleaseSigning") == "true") {
            create("release") {
                storeFile = file("../release.keystore", PathValidation.EXISTS)
                storePassword = findStringProperty("konceptStorePassword") ?: "android"
                keyAlias = findStringProperty("konceptKeyAlias") ?: "androiddebugkey"
                keyPassword = findStringProperty("konceptKeyPassword") ?: "android"
            }
        }
    }
    buildTypes {
        release {
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isShrinkResources = false
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        all {
            if (findProperty("enableReleaseSigning") == "true") {
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
        getByName("test").java.srcDirs("src/test/kotlin")
        getByName("androidTest").java.srcDirs("src/androidTest/kotlin")
    }
    buildFeatures {
        compose = true
        buildConfig = true
        aidl = false
        renderScript = false
        resValues = false
        shaders = false
    }
    packaging {
        resources {
            excludes += setOf(
                "/META-INF/{AL2.0,LGPL2.1}",
                "META-INF/LICENSE*"
            )
        }
    }
    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }
    testOptions {
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
        managedDevices {
            devices {
                create<ManagedVirtualDevice>("pixel6api33") {
                    device = "Pixel 6"
                    apiLevel = 33
                    systemImageSource = "aosp-atd"
                }
            }
        }
    }
}

dependencies {
    implementation(projects.common.commonDomain)
    implementation(projects.common.commonRemote)
    implementation(projects.common.commonUi)
    implementation(projects.base.baseNavigation)
    implementation(projects.design.designSystem)

    implementation(projects.features.dogs.dogsData)
    implementation(projects.features.dogs.dogsDomain)
    implementation(projects.features.dogs.dogsRemote)
    implementation(projects.features.dogs.dogsCache)
    implementation(projects.features.dogs.dogsUi)

    implementation(libs.kotlinx.coroutines)

    implementation(libs.androidx.core)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewModel)
    implementation(libs.androidx.lifecycle.compose)
    implementation(libs.androidx.compose.viewmodel)
    implementation(libs.androidx.compose.activity)
    implementation(libs.androidx.webkit)
    implementation(libs.androidx.splashscreen)

    implementation(platform(libs.compose.bom))
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material.icons)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.navigation)
    implementation(libs.androidx.constraintlayout.compose)

    implementation(libs.androidx.compose.uiToolingPreview)
    debugImplementation(libs.androidx.compose.uiTooling)

    coreLibraryDesugaring(libs.android.desugar)

    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.androidx.hilt.compiler)
    ksp(libs.hilt.compiler)

    implementation(libs.androidx.room)
    ksp(libs.androidx.room.compiler)

    implementation(libs.androidx.datastore)
    implementation(libs.androidx.datastore.preferences)

    implementation(libs.square.moshi)
    ksp(libs.square.moshi.codegen)

    implementation(libs.accompanist.nav.material)

    implementation(libs.timber)

    implementation(platform(libs.arrow.bom))
    implementation(libs.arrow.core)
    implementation(libs.arrow.retrofit)

    implementation(platform(libs.square.okhttp.bom))
    implementation(libs.square.okhttp)
    implementation(libs.square.okhttp.interceptor)

    implementation(libs.square.retrofit)
    implementation(libs.square.retrofit.moshi)

    // testing

    testImplementation(projects.features.dogs.dogsTest)

    testImplementation(libs.bundles.test)
    testImplementation(libs.bundles.mockito)

    testImplementation(libs.junit4)
    testImplementation(platform(libs.junit5.bom))
    testImplementation(libs.junit5.api)
    testRuntimeOnly(libs.junit5.engine)
    testRuntimeOnly(libs.junit5.vintage.engine)

    testImplementation(libs.robolectric)

    testImplementation(libs.androidx.test.core)

    testImplementation(libs.hilt.android.test)
    kspTest(libs.hilt.compiler)

    testImplementation(platform(libs.compose.bom))
    testImplementation(libs.androidx.compose.uiTest)
    testDebugImplementation(libs.androidx.compose.uiManifestTest)

    testImplementation(platform(libs.square.okhttp.bom))
    testImplementation(libs.square.okhttp.mockwebserver)

    testImplementation(libs.androidx.room.testing)

    baselineProfile(projects.baselineprofile)

    // android testing

    androidTestImplementation(projects.features.dogs.dogsTest)

    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.rules)

    androidTestImplementation(libs.androidx.test.espresso)
    androidTestImplementation(libs.androidx.test.espresso.intents)
    androidTestImplementation(libs.androidx.test.espresso.web)

    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.androidx.compose.uiTest)

    androidTestImplementation(libs.hilt.android.test)
    kspAndroidTest(libs.hilt.compiler)

    androidTestUtil(libs.androidx.test.orchestrator)

    androidTestImplementation(libs.mockk)
    androidTestImplementation(libs.robolectric.annotations)
}

fun Project.findStringProperty(propertyName: String, ciPropertyName: String = propertyName): String? {
    return if (CiUtils.isCI) {
        System.getenv(ciPropertyName)
    } else {
        findProperty(propertyName) as String? ?: run {
            println("$propertyName missing in gradle.properties")
            null
        }
    }
}
