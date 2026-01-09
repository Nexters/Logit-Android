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
    }
}