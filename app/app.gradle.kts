plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}
android {
    compileSdk = 31
    defaultConfig {
        applicationId = "de.nilsdruyen.koncept"
        minSdk = 24
        targetSdk = 31
        versionCode = 1
        versionName = "0.0.1"

        buildConfigField(
            "String",
            "DOG_API_KEY",
            "\"${findStringProperty("dogApiKey")}\""
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
                storePassword = findStringProperty("myBoardGamesStorePassword")
                keyAlias = findStringProperty("myBoardGamesKeyAlias")
                keyPassword = findStringProperty("myBoardGamesKeyPassword")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).all {
        kotlinOptions {
            jvmTarget = "11"
            freeCompilerArgs = listOf(
                "-progressive",
                "-Xopt-in=kotlin.RequiresOptIn",
            )
        }
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
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    kapt {
        correctErrorTypes = true
        arguments {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }
}

configurations {
// https://github.com/ben-manes/gradle-versions-plugin/issues/393#issuecomment-726275755
    create("dependencyUpdatesConfig") {
        isCanBeResolved = false
        isCanBeConsumed = true
    }
}
//val dependencyUpdatesConfig by configurations
dependencies {
    implementation(projects.commonDomain)
    implementation(projects.commonRemote)
    implementation(projects.commonUi)

//    implementation(projects.dogsRemote)
    implementation(projects.dogsCache)
    implementation(projects.dogsUi)

    implementation(libs.core)
    implementation(libs.coroutines)

    implementation(libs.lifecycleRuntime)
    implementation(libs.viewModel)
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

//    dependencyUpdatesConfig(libs.desugar)
    coreLibraryDesugaring(libs.desugar)

    implementation(libs.hilt)
    implementation(libs.hiltNavigation)
    kapt(libs.hiltCompiler)

    implementation(libs.room)
    kapt(libs.roomCompiler)

    implementation(libs.datastore)

    implementation(libs.moshi)
    kapt(libs.moshiCompiler)

    implementation(libs.accompanistSystemUi)
    implementation(libs.accompanistInsets)
    implementation(libs.accompanistNavAnim)
    implementation(libs.accompanistNavMaterial)

    implementation(libs.timber)
    implementation(platform(libs.arrowStack))
    implementation(libs.arrowKt)
    implementation(libs.arrowKtRetrofit)

    implementation(platform(libs.okHttpBom))
    implementation(libs.okHttp)
    implementation(libs.okHttpInterceptor)

    implementation(libs.retrofit)
    implementation(libs.retrofitMoshi)

//    testImplementation(libs.junit4)
//    implementation(libs.junit4)
}

fun Project.findStringProperty(propertyName: String): String? {
    return findProperty(propertyName) as String? ?: run {
        println("$propertyName missing in gradle.properties")
        null
    }
}