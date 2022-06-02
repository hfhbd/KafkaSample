plugins {
    kotlin("multiplatform")
}

repositories {
    mavenCentral()
}

kotlin {
    jvm()

    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(projects.shared)

                implementation(projects.streaming)
                implementation(projects.mocker)
                implementation(projects.converter)
                implementation(projects.backend)

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.2")
            }
        }
    }
}
