package de.nilsdruyen.app.config

import com.android.build.gradle.BaseExtension
import kotlinx.kover.api.KoverTaskExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

@Suppress("UnstableApiUsage")
internal fun Project.configureKotlinAndroid() {
    configure<BaseExtension> {
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }
        testOptions {
            unitTests {
                isIncludeAndroidResources = true
                isReturnDefaultValues = true
            }
//            unitTests.all {
//                if (it.name == "testDebugUnitTest") {
//                    it.extensions.configure(KoverTaskExtension::class) {
//                        isDisabled = false
////                    binaryReportFile.set(file("$buildDir/custom/debug-report.bin"))
////                    includes = listOf("com.example.*")
////                    excludes = listOf("com.example.subpackage.*")
//                    }
//                }
//            }
        }
        sourceSets {
            getByName("main").java.srcDirs("src/main/kotlin")
            getByName("test").java.srcDirs("src/test/kotlin")
            getByName("androidTest").java.srcDirs("src/androidTest/kotlin")
        }
    }
}