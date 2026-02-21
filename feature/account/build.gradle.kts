import com.android.build.api.dsl.LibraryDefaultConfig
import java.util.Properties

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

    defaultConfig {
        configureBuildConfigs()
    }
}

fun LibraryDefaultConfig.configureBuildConfigs() {
    val googleOauthClientId = project.rootProject.file("local.properties").let { file ->
        val properties = Properties()
        if (file.exists()) properties.load(file.inputStream())
        properties.getProperty("GOOGLE_OAUTH_CLIENT_ID") ?: ""
    }

    buildConfigField("String", "GOOGLE_OAUTH_CLIENT_ID", "\"$googleOauthClientId\"")
}
