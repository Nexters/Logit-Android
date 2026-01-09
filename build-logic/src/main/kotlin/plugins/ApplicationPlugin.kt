package plugins

import extensions.application
import extensions.catalog
import extensions.implementation
import extensions.kotlin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class ApplicationPlugin: Plugin<Project> {

    companion object {
        private const val APPLICATION_ID = "com.useai.logit"
        private const val TARGET_SDK = 36
        private const val MIN_SDK = 28
        private const val JAVA_VERSION = 17

        private const val MAJOR_VERSION = 1
        private const val MINOR_VERSION = 0
        private const val PATCH_VERSION = 0
        private const val VERSION_CODE = (MAJOR_VERSION * 10000) + (MINOR_VERSION * 100) + PATCH_VERSION
        private const val VERSION_NAME = "$MAJOR_VERSION.$MINOR_VERSION.$PATCH_VERSION"
    }

    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("com.google.devtools.ksp")
            }

            kotlin {
                jvmToolchain(JAVA_VERSION)
            }

            configureApplicationInfo()

            dependencies {
                implementation(catalog.findLibrary("androidx-core-ktx").get())
                implementation(catalog.findLibrary("kotlinx-serialization-json").get())
            }
        }
    }

    private fun Project.configureApplicationInfo() {
        application {
            namespace = APPLICATION_ID
            compileSdk {
                version = release(TARGET_SDK) {
                    minorApiLevel = 1
                }
            }

            defaultConfig {
                applicationId = APPLICATION_ID
                minSdk = MIN_SDK
                targetSdk = TARGET_SDK
                versionCode = VERSION_CODE
                versionName = VERSION_NAME

                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }

            buildTypes {
                release {
                    isMinifyEnabled = false
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "proguard-rules.pro"
                    )
                }
            }

            buildFeatures {
                compose = true
                buildConfig = true
            }
        }
    }
}