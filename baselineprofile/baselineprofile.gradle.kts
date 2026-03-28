import com.android.build.api.dsl.ManagedVirtualDevice
import com.android.build.api.dsl.TestExtension
import de.nilsdruyen.app.ProjectConfig
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidTest)
    alias(libs.plugins.androidx.baselineprofile)
}

configure<TestExtension> {
    namespace = "de.nilsdruyen.koncept.baseline"
    compileSdk = ProjectConfig.compileSdkVersion

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    defaultConfig {
        minSdk = 28
        targetSdk = ProjectConfig.targetSdkVersion

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    targetProjectPath = ":app"

    testOptions.managedDevices.allDevices {
        create<ManagedVirtualDevice>("pixel6api33") {
            device = "Pixel 6"
            apiLevel = 33
            systemImageSource = "aosp-atd"
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_21
    }
}

// This is the configuration block for the Baseline Profile plugin.
// You can specify to run the generators on a managed devices or connected devices.
baselineProfile {
    managedDevices += "pixel6api33"
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
            v.testedApks.map { v.artifacts.getBuiltArtifactsLoader().load(it)?.applicationId.orEmpty() }
        )
    }
}