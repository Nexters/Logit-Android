package extensions

import org.gradle.api.artifacts.dsl.DependencyHandler

internal fun DependencyHandler.implementation(dependency: Any) {
    add("implementation", dependency)
}

internal fun DependencyHandler.compileOnly(dependency: Any) {
    add("compileOnly", dependency)
}

internal fun DependencyHandler.api(dependency: Any) {
    add("api", dependency)
}

internal fun DependencyHandler.ksp(dependency: Any) {
    add("ksp", dependency)
}