plugins {
    alias(libs.plugins.logit.kotlin.library.common)
}

dependencies {
    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines.core)
}