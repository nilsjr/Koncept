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
    tasks.register("allJacocoReports") {
        dependsOn(subprojects.map { it.tasks.withType(JacocoReport::class) }.flatten())
    }
    tasks.register<JacocoReport>("mergeCoverage") {
        group = "Reporting"
        description = "Generate overall Jacoco coverage report for the debug build."

        dependsOn(tasks.named("allJacocoReports"))

        reports {
            html.required.set(true)
            xml.required.set(true)
        }

        applyAllProjectDirectories(this@afterEvaluate)

        doLast {
            println("View code coverage at:")
            println("file://$buildDir/reports/jacoco/mergeCoverage/html/index.html")
        }
    }

    tasks.register<JacocoCoverageVerification>("allCoverageVerification") {
        applyAllProjectDirectories(this@afterEvaluate)
        applyRules()
    }
}
