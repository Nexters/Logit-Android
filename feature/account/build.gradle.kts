plugins {
    alias(libs.plugins.logit.android.library.common)
    alias(libs.plugins.logit.compose.common)
    alias(libs.plugins.logit.hilt)
    alias(libs.plugins.logit.circuit)
    alias(libs.plugins.logit.feature.common.dependencies)
    alias(libs.plugins.logit.googleid)
}

android {
    namespace = "com.useai.feature.account"
}
