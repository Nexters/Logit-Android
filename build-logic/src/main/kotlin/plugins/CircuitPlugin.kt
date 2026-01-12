package plugins

import extensions.catalog
import extensions.implementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class CircuitPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("kotlin-parcelize")
            }

            dependencies {
                implementation(catalog.findLibrary("circuit").get())
            }
        }
    }
}