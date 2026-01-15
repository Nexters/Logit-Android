package plugins

import extensions.catalog
import extensions.implementation
import extensions.ksp
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class CircuitPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("kotlin-parcelize")
            }

            ksp {
                arg("circuit.codegen.mode", "hilt")
            }

            dependencies {
                implementation(catalog.findBundle("bundle-circuit").get())
                implementation(catalog.findLibrary("circuit-codegen-annotations").get())
                ksp(catalog.findLibrary("circuit-codegen").get())
            }
        }
    }
}