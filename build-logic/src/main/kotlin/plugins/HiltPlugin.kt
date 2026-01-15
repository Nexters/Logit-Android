package plugins

import extensions.catalog
import extensions.implementation
import extensions.ksp
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class HiltPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("com.google.dagger.hilt.android")
            }

            dependencies {
                implementation(catalog.findLibrary("hilt-android").get())
                ksp(catalog.findLibrary("hilt-compiler").get())
            }
        }
    }
}