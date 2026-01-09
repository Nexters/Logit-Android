package plugins

import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidLibraryCommonPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("com.google.devtools.ksp")
            }
        }
    }
}