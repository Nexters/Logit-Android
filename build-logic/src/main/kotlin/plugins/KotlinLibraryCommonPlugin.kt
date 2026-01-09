package plugins

import constants.JAVA_VERSION
import extensions.kotlin
import org.gradle.api.Plugin
import org.gradle.api.Project

class KotlinLibraryCommonPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("java-library")
                apply("org.jetbrains.kotlin.jvm")
            }

            kotlin {
                jvmToolchain(JAVA_VERSION)
            }
        }
    }
}