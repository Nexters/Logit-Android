package plugins

import constants.JAVA_VERSION
import constants.MIN_SDK
import constants.SDK_MINOR_API_LEVEL
import constants.TARGET_SDK
import extensions.android
import extensions.kotlinAndroid
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

            configureLibraryInfo()
        }
    }

    private fun Project.configureLibraryInfo() {
        android {
            kotlinAndroid {
                jvmToolchain(JAVA_VERSION)
            }
            
            compileSdk {
                version = release(TARGET_SDK) {
                    minorApiLevel = SDK_MINOR_API_LEVEL
                }
            }

            defaultConfig {
                minSdk = MIN_SDK

                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                consumerProguardFiles("consumer-rules.pro")
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
                buildConfig = true
            }
        }
    }
}