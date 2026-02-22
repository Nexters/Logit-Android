plugins {
    alias(libs.plugins.logit.kotlin.library.common)
    alias(libs.plugins.logit.hilt)
}

dependencies {
    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines.core)
}
