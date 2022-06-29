plugins {
    id("de.nilsdruyen.plugin.kotlin")
    kotlin("kapt")
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
    implementation(projects.dogsDomain)
    implementation(projects.dogsEntity)

    implementation(libs.hiltCore)
    kapt(libs.hiltCompiler)

    implementation(libs.coroutines)

    implementation(platform(libs.arrowStack))
    implementation(libs.arrowKt)

    testImplementation(libs.bundles.test)
    testImplementation(projects.dogsTest)
    testImplementation(projects.commonTest)

    testImplementation(platform(libs.junit5Bom))
    testImplementation(libs.junit5Api)
    testRuntimeOnly(libs.junit5Engine)

    testImplementation(libs.bundles.mockito)
    testImplementation(libs.mockitoJupiter)
}