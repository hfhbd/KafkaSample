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
    val ktor = "2.3.2"
    implementation("io.ktor:ktor-client-core:$ktor")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor")
    implementation("io.ktor:ktor-client-resources:$ktor")
    implementation(projects.shared)

    implementation(compose.web.core)
    implementation("app.softwork:bootstrap-compose:0.1.15")

    testImplementation(kotlin("test"))
}

licensee {
    allow("Apache-2.0")
}
