pluginManagement {
    repositories {
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        gradlePluginPortal()
    }
}

rootProject.name = "KafkaSample"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":shared")

include(":backend")
include(":frontend")
include(":mocker")
include(":streaming")

include(":demo")
