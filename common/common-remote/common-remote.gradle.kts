plugins {
    id("kotlin")
    kotlin("kapt")
}

dependencies {
    implementation(projects.commonDomain)

    implementation(libs.retrofit)
    implementation(libs.retrofitMoshi)

    implementation(platform(libs.okHttpBom))
    implementation(libs.okHttp)
    implementation(libs.okHttpInterceptor)

    implementation(libs.hiltCore)
    kapt(libs.hiltCompiler)

    implementation(platform(libs.arrowStack))
    implementation(libs.arrowKt)
}