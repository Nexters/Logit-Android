import com.android.build.api.dsl.LibraryDefaultConfig
import java.util.Properties

plugins {
    alias(libs.plugins.logit.android.library.common)
    alias(libs.plugins.logit.hilt)
}

android {
    namespace = "com.useai.core.network"

    defaultConfig {
        configureBuildConfigs()
    }
}

dependencies {
    api(projects.core.modelError)
    api(projects.core.common)
    api(projects.core.model)

    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.okhttp.event.source)

    implementation(libs.kotlinx.serialization.json)
}

fun LibraryDefaultConfig.configureBuildConfigs() {
    val localProperties = Properties()
    val localPropertiesFile: File? = rootProject.file("local.properties")
    if (localPropertiesFile?.exists() == true)
        localProperties.load(localPropertiesFile.inputStream())

    val baseUrl = localProperties.getProperty("BASE_URL") ?: error("local.properties에 BASE_URL을 선언해주세요")
    buildConfigField("String", "BASE_URL", "\"$baseUrl\"")

    val testUserAccessToken = localProperties.getProperty("TEST_USER_ACCESS_TOKEN") ?: error("local.properties에 TEST_USER_ACCESS_TOKEN을 선언해주세요")
    buildConfigField("String", "TEST_USER_ACCESS_TOKEN", "\"$testUserAccessToken\"")
}
