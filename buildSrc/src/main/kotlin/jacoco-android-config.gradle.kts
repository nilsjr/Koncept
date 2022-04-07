apply(plugin = "jacoco")

configure<JacocoPluginExtension> {
    toolVersion = JacocoConfig.toolVersion
}

tasks.withType<Test> {
    configure<JacocoTaskExtension> {
        isIncludeNoLocationClasses = true
        excludes = listOf("jdk.internal.*")
    }
}

afterEvaluate {
    tasks.register<JacocoReport>("jacocoTestReport") {
        group = "reporting"
        description = "Generates test coverage report"

        dependsOn("testDebugUnitTest")

        reports {
            html.required.set(true)
            xml.required.set(true)
        }

        applyDirectories(this@afterEvaluate)

        doLast {
            println("View code coverage at:")
            println("file://$buildDir/reports/jacoco/jacocoTestReport/html/index.html")
        }
    }
    tasks.register<JacocoCoverageVerification>("jacocoTestCoverageVerification") {
        applyDirectories(this@afterEvaluate)
        applyRules()
    }
}
