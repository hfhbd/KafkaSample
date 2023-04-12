pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories { 
        mavenCentral()
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
