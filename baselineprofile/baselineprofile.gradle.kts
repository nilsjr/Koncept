import com.android.build.api.dsl.ManagedVirtualDevice
import de.nilsdruyen.app.ProjectConfig

plugins {
    alias(libs.plugins.androidTest)
    alias(libs.plugins.kotlin.androidGradle)
    alias(libs.plugins.androidx.baselineprofile)
}

android {
    namespace = "de.nilsdruyen.koncept.baseline"
    compileSdk = ProjectConfig.compileSdkVersion

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    defaultConfig {
        minSdk = 28
        targetSdk = ProjectConfig.targetSdkVersion

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    targetProjectPath = ":app"

    testOptions.managedDevices.devices {
        create<ManagedVirtualDevice>("pixel7api33") {
            device = "Pixel 7"
            apiLevel = 33
            systemImageSource = "google-atd"
        }
    }
}

// This is the configuration block for the Baseline Profile plugin.
// You can specify to run the generators on a managed devices or connected devices.
baselineProfile {
    managedDevices += "pixel7api33"
    useConnectedDevices = false
}

dependencies {
    implementation(libs.androidx.test.junit)
    implementation(libs.androidx.test.espresso)
    implementation(libs.uiautomator)
    implementation(libs.benchmark.macro.junit4)
}

androidComponents {
    onVariants { v ->
        v.instrumentationRunnerArguments.put(
            "targetAppId",
            v.testedApks.map { v.artifacts.getBuiltArtifactsLoader().load(it)?.applicationId }
        )
    }
}