plugins {
    alias(libs.plugins.logit.application)
    alias(libs.plugins.logit.compose.common)
    alias(libs.plugins.logit.hilt)
    alias(libs.plugins.logit.circuit)
    alias(libs.plugins.logit.splashscreen)
}

dependencies {
    implementation(projects.feature.home)
    implementation(projects.feature.account)
    implementation(projects.feature.chat)
    implementation(projects.feature.newproject)
    implementation(projects.feature.experience)
    implementation(projects.feature.report)
    implementation(projects.core.designsystem)
    implementation(projects.core.navigation)
    implementation(projects.core.data)
    implementation(projects.core.common)
    implementation(projects.core.model)
    implementation(projects.core.modelError)
    implementation(projects.core.datastore)
    implementation(projects.core.network)
}
