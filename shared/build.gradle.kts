plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("app.cash.licensee")
}

kotlin {
    jvmToolchain(11)

    jvm()
    js(IR) {
        browser()
        binaries.executable()
    }

    sourceSets {
        commonMain {
            dependencies {
                api(libs.ktor.resources)
                api(libs.serialization.json)
                api(libs.datetime)
            }
        }

        named("jvmMain") {
            dependencies {
                runtimeOnly(libs.logback)
            }
        }
    }
}

licensee {
    allow("Apache-2.0")
    allow("MIT")
    allow("EPL-1.0")
}
