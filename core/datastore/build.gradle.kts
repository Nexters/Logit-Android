plugins {
    alias(libs.plugins.logit.android.library.common)
    alias(libs.plugins.logit.hilt)
}

android {
    namespace = "com.useai.core.datastore"
}

dependencies {
    api(projects.core.common)

    implementation(libs.androidx.datastore.preferences)
}
