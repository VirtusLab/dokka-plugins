include("dokka-tooltip-generator-plugin")
include("dokka-tooltip-installer-plugin")
include("dokka-tooltip-gradle-runner")

pluginManagement {
    val kotlin_version: String by settings
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "org.jetbrains.dokka") {
                useModule("org.jetbrains.dokka:dokka-gradle-plugin:${requested.version}")
            }
        }
    }
    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
        gradlePluginPortal()
        maven("https://dl.bintray.com/kotlin/kotlin-dev")
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
    }
}
rootProject.name = "plugins"
