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
                val ktor = "2.0.2"
                api("io.ktor:ktor-resources:$ktor")
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
            }
        }

        val jvmMain by getting {
            dependencies {
                runtimeOnly("ch.qos.logback:logback-classic:1.2.11")
            }
        }
    }
}
