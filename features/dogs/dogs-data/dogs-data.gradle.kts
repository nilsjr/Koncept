plugins {
    id("kotlin")
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

    implementation(libs.coroutines)
    implementation(libs.arrowKt)

    testImplementation(projects.dogsTest)
    testImplementation(projects.commonTest)
    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.junitEngine)
}