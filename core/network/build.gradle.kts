plugins {
    alias(libs.plugins.logit.android.library.common)
    alias(libs.plugins.logit.hilt)
}

android {
    namespace = "com.useai.core.network"
}

dependencies {
    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.okhttp.sse)

    implementation(libs.kotlinx.serialization.json)
}