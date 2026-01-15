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

internal fun DependencyHandler.debugImplementation(dependency: Any) {
    add("debugImplementation", dependency)
}

internal fun DependencyHandler.androidTestImplementation(dependency: Any) {
    add("androidTestImplementation", dependency)
}

internal fun DependencyHandler.testImplementation(dependency: Any) {
    add("testImplementation", dependency)
}
