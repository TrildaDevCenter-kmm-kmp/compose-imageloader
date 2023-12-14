pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
    includeBuild("build-logic")
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    // repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://jitpack.io")
    }
}

plugins {
    id("com.gradle.enterprise") version "3.16"
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
        publishAlwaysIf(System.getenv("GITHUB_ACTIONS") != null)
    }
}

rootProject.name = "compose-imageLoader"

include(
    ":image-loader",
    ":image-loader-singleton",
    ":extension:blur",
    ":extension:imageio",
    ":extension:ktor-network",
    ":extension:moko-resources",
    ":extension:nine-patch",
    ":app:common",
    ":app:android",
    ":app:android:benchmark",
    ":app:desktop",
    ":app:ios-combine",
    ":app:macos",
    ":app:web",
    ":app:intellij-plugin",
)

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
