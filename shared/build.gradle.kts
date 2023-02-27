plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("app.cash.licensee")
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
                val ktor = "2.2.3"
                api("io.ktor:ktor-resources:$ktor")
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
                api("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
            }
        }

        val jvmMain by getting {
            dependencies {
                runtimeOnly("ch.qos.logback:logback-classic:1.4.5")
            }
        }
    }
}

licensee {
    allow("Apache-2.0")
    allow("MIT")
}
