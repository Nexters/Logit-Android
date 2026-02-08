plugins {
    alias(libs.plugins.logit.android.library.common)
    alias(libs.plugins.logit.hilt)
    alias(libs.plugins.logit.compose.common)
    alias(libs.plugins.logit.circuit)
}

android {
    namespace = "com.useai.core.ui"
}

dependencies {
    api(projects.core.designsystem)
    api(projects.core.model)
}
