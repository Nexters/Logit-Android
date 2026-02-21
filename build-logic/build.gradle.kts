plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("applicationPlugin") {
            id = "com.useai.plugin.application"
            implementationClass = "plugins.ApplicationPlugin"
        }
        register("androidComposeCommonPlugin") {
            id = "com.useai.plugin.android.compose.common"
            implementationClass = "plugins.AndroidComposeCommonPlugin"
        }
        register("hiltPlugin") {
            id = "com.useai.plugin.hilt"
            implementationClass = "plugins.HiltPlugin"
        }
        register("kotlinLibraryCommonPlugin") {
            id = "com.useai.plugin.kotlin.library.common"
            implementationClass = "plugins.KotlinLibraryCommonPlugin"
        }
        register("androidLibraryCommonPlugin") {
            id = "com.useai.plugin.android.library"
            implementationClass = "plugins.AndroidLibraryCommonPlugin"
        }
        register("circuitPlugin") {
            id = "com.useai.plugin.circuit"
            implementationClass = "plugins.CircuitPlugin"
        }
        register("androidFeatureCommonDependenciesPlugin") {
            id = "com.useai.plugin.android.feature.common.dependencies"
            implementationClass = "plugins.AndroidFeatureCommonDependenciesPlugin"
        }
        register("splashscreenPlugin") {
            id = "com.useai.plugin.splashscreen"
            implementationClass = "plugins.SplashscreenPlugin"
        }
        register("googleidPlugin") {
            id = "com.useai.plugin.googleid"
            implementationClass = "plugins.GoogleIdPlugin"
        }
        register("datastorePlugin") {
            id = "com.useai.plugin.datastore"
            implementationClass = "plugins.DataStorePlugin"
        }
    }
}
