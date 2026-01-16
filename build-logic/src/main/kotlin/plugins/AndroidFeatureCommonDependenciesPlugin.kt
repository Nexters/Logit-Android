package plugins

import extensions.api
import extensions.implementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

class AndroidFeatureCommonDependenciesPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        target.run {

            dependencies {
                implementation(project(":core:data"))
                api(project(":core:designsystem"))
                api(project(":core:ui"))
                api(project(":core:model"))
                api(project(":core:model-error"))
                api(project(":core:common"))
                api(project(":core:navigation"))
            }
        }
    }
}
