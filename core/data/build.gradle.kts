plugins {
    alias(libs.plugins.logit.android.library.common)
    alias(libs.plugins.logit.hilt)
}

android {
    namespace = "com.useai.core.data"
}

dependencies {
    api(projects.core.model)
    implementation(projects.core.network)
    implementation(projects.core.datastore)
}
