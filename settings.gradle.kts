pluginManagement {
    repositories {
        google()
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

rootProject.name = "stamp_tour_app"
include(":app")
include(":core_network")
include(":core_model")
include(":feature_home")
include(":core_ui")
include(":core_utils")
include(":common")
include(":feature_auth")
include(":feature_near_place")
include(":feature_my_tour")
include(":feature_my_tour_detail")
