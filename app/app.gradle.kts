import de.nilsdruyen.app.ProjectConfig

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("de.nilsdruyen.plugin.android.application")
    id(libs.plugins.kotlin.kapt.get().pluginId)
    id(libs.plugins.google.ksp.get().pluginId)
    id(libs.plugins.hilt.android.get().pluginId)
}
android {
    compileSdk = ProjectConfig.compileSdkVersion
    defaultConfig {
        applicationId = "de.nilsdruyen.koncept"
        minSdk = 24
        targetSdk = 32
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
                "-opt-in=kotlin.RequiresOptIn",
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
    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }
}

dependencies {
    implementation(projects.commonDomain)
    implementation(projects.commonRemote)
    implementation(projects.commonUi)

    implementation(projects.dogsRemote)
    implementation(projects.dogsCache)
    implementation(projects.dogsUi)

    implementation(libs.androidx.core)
    implementation(libs.kotlinx.coroutines)

    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewModel)
    implementation(libs.androidx.compose.viewmodel)
    implementation(libs.androidx.compose.activity)

    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material.icons)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.navigation)
    implementation(libs.androidx.compose.constraint)

    implementation(libs.androidx.compose.uiToolingPreview)
    debugImplementation(libs.androidx.compose.uiTooling)

    coreLibraryDesugaring(libs.android.desugar)

    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    kapt(libs.androidx.hilt.compiler)
    kapt(libs.hilt.compiler)

    implementation(libs.androidx.room)
    ksp(libs.androidx.room.compiler)

    implementation(libs.androidx.datastore)
    implementation(libs.androidx.datastore.preferences)

    implementation(libs.square.moshi)
    ksp(libs.square.moshi.codegen)

    implementation(libs.accompanist.systemUiController)
    implementation(libs.accompanist.nav.anim)
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

    implementation(libs.fornewid.compose.motion.core)
    implementation(libs.fornewid.compose.motion.navigation)
}

fun Project.findStringProperty(propertyName: String): String? {
    return findProperty(propertyName) as String? ?: run {
        println("$propertyName missing in gradle.properties")
        null
    }
}