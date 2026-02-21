plugins {
    alias(libs.plugins.logit.android.library.common)
    alias(libs.plugins.logit.hilt)
}

android {
    namespace = "com.useai.core.data"
}

dependencies {
    api(projects.core.model)
    api(projects.core.datastore)
    implementation(projects.core.network)
}
