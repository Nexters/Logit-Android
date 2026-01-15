plugins {
    alias(libs.plugins.logit.application)
    alias(libs.plugins.logit.compose.common)
    alias(libs.plugins.logit.hilt)
    alias(libs.plugins.logit.circuit)
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.core.navigation)
}
