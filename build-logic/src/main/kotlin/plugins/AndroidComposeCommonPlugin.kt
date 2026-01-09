package plugins

import extensions.catalog
import extensions.implementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidComposeCommonPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            dependencies {
                implementation(platform(catalog.findLibrary("androidx-compose-bom").get()))
                implementation(catalog.findBundle("bundle-compose").get())
                implementation(catalog.findBundle("bundle-android-navigation3").get())
                implementation(catalog.findLibrary("hilt-compose").get())
            }
        }
    }
}