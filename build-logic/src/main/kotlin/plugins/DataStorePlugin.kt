package plugins

import extensions.api
import extensions.catalog
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class DataStorePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                api(catalog.findLibrary("androidx-datastore-preferences").get())
                api(catalog.findLibrary("androidx-datastore-core").get())
            }
        }
    }
}
