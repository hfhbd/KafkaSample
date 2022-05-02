plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

repositories {
    mavenCentral()
}

kotlin {
    jvm()
    js(IR) {
        browser()
        binaries.executable()
    }

    sourceSets {
        commonMain {
            dependencies {
                api("io.ktor:ktor-resources:2.0.1")
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
            }
        }

        val jvmMain by getting {
            dependencies {
                runtimeOnly("ch.qos.logback:logback-classic:1.2.11")
            }
        }
    }
}
