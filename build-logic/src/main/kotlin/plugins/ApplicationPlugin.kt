package plugins

import constants.JAVA_VERSION
import constants.MIN_SDK
import constants.TARGET_SDK
import constants.VERSION_CODE
import constants.VERSION_NAME
import extensions.application
import extensions.catalog
import extensions.implementation
import extensions.kotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class ApplicationPlugin: Plugin<Project> {

    companion object {
        private const val APPLICATION_ID = "com.useai.logit"
    }

    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("com.google.devtools.ksp")
            }

            kotlinAndroid {
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