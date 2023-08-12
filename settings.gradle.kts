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
        maven {
            url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
            authentication {
                create<BasicAuthentication>("basic")
            }
            credentials {
                username = "mapbox"
                password = extra["MAPBOX_DOWNLOADS_TOKEN"] as String
//                password = "sk.eyJ1Ijoibmd1eWVuMTk5OSIsImEiOiJjbGR1aW81cWMwNjZwM29wbm5xcnZuaWk4In0.QrCA3zeZNbcoVwJsC2MdjA"
            }
        }
    }
}

rootProject.name = "syl_v2"
include(":app")
