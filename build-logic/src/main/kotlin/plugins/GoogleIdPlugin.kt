package plugins

import com.android.build.gradle.BaseExtension
import extensions.catalog
import extensions.implementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import java.util.Properties

class GoogleIdPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                implementation(catalog.findBundle("bundle-googleid").get())
            }

            val properties = Properties().apply {
                val propertiesFile = rootProject.file("local.properties")
                if (propertiesFile.exists()) {
                    load(propertiesFile.inputStream())
                }
            }
            val googleOauthClientId = properties.getProperty("GOOGLE_OAUTH_CLIENT_ID") ?: ""
            val androidExtension = extensions.findByType(BaseExtension::class.java)
            androidExtension?.apply {
                buildFeatures.buildConfig = true
                defaultConfig {
                    buildConfigField("String", "GOOGLE_OAUTH_CLIENT_ID", "\"$googleOauthClientId\"")
                }
            }
        }
    }
}
