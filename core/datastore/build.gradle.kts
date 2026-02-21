plugins {
    alias(libs.plugins.logit.android.library.common)
    alias(libs.plugins.logit.hilt)
    alias(libs.plugins.logit.datastore)
}

android {
    namespace = "com.useai.core.datastore"
}

dependencies {
    api(projects.core.common)
}
