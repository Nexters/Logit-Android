plugins {
    alias(libs.plugins.logit.android.library.common)
    alias(libs.plugins.logit.hilt)
}

android {
    namespace = "com.useai.core.network"
}

dependencies {
    api(projects.core.modelError)
    api(projects.core.common)
    api(projects.core.model)

    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.okhttp.event.source)

    implementation(libs.kotlinx.serialization.json)
}
