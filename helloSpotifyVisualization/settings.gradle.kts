pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        flatDir {
            name = "Local libs"
            dirs = setOf(File("libs"))
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "HelloSpotifyVisualization"
include(":app")
