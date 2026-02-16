plugins {
    alias(libs.plugins.logit.android.library.common)
    alias(libs.plugins.logit.compose.common)
    alias(libs.plugins.logit.hilt)
    alias(libs.plugins.logit.circuit)
    alias(libs.plugins.logit.feature.common.dependencies)
}

android {
    namespace = "com.useai.feature.account"
}
