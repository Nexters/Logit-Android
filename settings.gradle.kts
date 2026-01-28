pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention").version("0.9.0")
}

rootProject.name = "Logit"
include(":app")
include(":feature")
include(":core")
include(":core:model")
include(":core:common")
include(":core:data")
include(":core:network")
include(":core:datastore")
include(":core:model-error")
include(":core:designsystem")
include(":core:ui")
include(":feature:chat")
include(":core:navigation")
include(":feature:home")
include(":feature:newproject")
include(":feature:experience")
include(":feature:report")
