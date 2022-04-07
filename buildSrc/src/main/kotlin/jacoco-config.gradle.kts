apply(plugin = "jacoco")

configure<JacocoPluginExtension> {
    toolVersion = JacocoConfig.toolVersion
}

tasks.withType<JacocoReport> {
    dependsOn(tasks.withType<Test>())

    reports {
        html.required.set(true)
        xml.required.set(true)
    }

    applyDirectories(project)

    doLast {
        println("View code coverage at:")
        println("file://$buildDir/reports/jacoco/test/html/index.html")
    }
}

tasks.withType<JacocoCoverageVerification> {
    applyDirectories(project)
    applyRules()
}
