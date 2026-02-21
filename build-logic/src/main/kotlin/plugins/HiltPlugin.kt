package plugins

import extensions.catalog
import extensions.implementation
import extensions.ksp
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class HiltPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.apply("com.google.devtools.ksp")

            val isAndroid = listOf("com.android.application", "com.android.library").any(pluginManager::hasPlugin)

            if (isAndroid) {
                pluginManager.apply("com.google.dagger.hilt.android")
            }

            dependencies {
                if (isAndroid) {
                    implementation(catalog.findLibrary("hilt-android").get())
                } else {
                    implementation(catalog.findLibrary("hilt-core").get())
                }
                ksp(catalog.findLibrary("hilt-compiler").get())
            }
        }
    }
}
