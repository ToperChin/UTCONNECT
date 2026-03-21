pluginManagement {
    repositories {
<<<<<<< HEAD
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
=======
        google()
>>>>>>> 2a84b58246ea5ec549d55d9ef95d8801c3333cba
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
<<<<<<< HEAD

rootProject.name = "UTCONNECT"
=======
rootProject.name = "UTConnect"
>>>>>>> 2a84b58246ea5ec549d55d9ef95d8801c3333cba
include(":app")
