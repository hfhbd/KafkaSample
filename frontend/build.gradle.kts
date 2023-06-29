plugins {
    kotlin("js")
    id("org.jetbrains.compose")
    id("app.cash.licensee")
}

kotlin {
    js(IR) {
        browser {
            binaries.executable()
            useCommonJs()
            commonWebpackConfig {
                scssSupport {
                    enabled.set(true)
                }
            }
        }
    }
}

dependencies {
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.resources)
    implementation(projects.shared)

    implementation(compose.web.core)
    implementation(libs.bootstrap.compose)

    testImplementation(kotlin("test"))
}

licensee {
    allow("Apache-2.0")
}
