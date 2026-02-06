pluginManagement {
    repositories {
        maven {
            url = uri("https://maven.google.com")
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven {
            url = uri("https://maven.google.com")
        }
        mavenCentral()
    }
}

rootProject.name = "ZeroThreat"
include(":app")
include(":core")
project(":core").projectDir = file("core/app")
 