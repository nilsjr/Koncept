package de.nilsdruyen.app.config

//fun Project.configureDetekt(vararg paths: String) {
//    configure<DetektExtension> {
//        toolVersion = "1.19.0"
//        source = files(paths)
//        parallel = true
//        config = files("$rootDir/config/detekt-config.yml")
//        buildUponDefaultConfig = true
//        ignoreFailures = false
//    }
//    tasks.withType<Detekt>().configureEach {
//        this.jvmTarget = "11"
//        reports {
//            xml {
//                required.set(true)
//                outputLocation.set(file("$buildDir/reports/detekt/detekt.xml"))
//            }
//            html.required.set(false)
//            txt.required.set(true)
//        }
//    }
//    dependencies {
//        "detektPlugins"("io.gitlab.arturbosch.detekt:detekt-formatting:1.19.0")
//    }
//}