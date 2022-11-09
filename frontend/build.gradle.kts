plugins {
    kotlin("js")
    id("org.jetbrains.compose")
    id("app.cash.licensee")
}

repositories {
    mavenCentral()
    maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    js(IR) {
        browser {
            binaries.executable()
            useCommonJs()
            commonWebpackConfig {
                cssSupport.enabled = true
            }
        }
    }
}

dependencies {
    val ktor = "2.1.3"
    implementation("io.ktor:ktor-client-core:$ktor")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor")
    implementation("io.ktor:ktor-client-resources:$ktor")
    implementation(projects.shared)

    implementation(compose.web.core)
    implementation("app.softwork:bootstrap-compose:0.1.12")
    implementation(devNpm("sass-loader", "^13.0.0"))
    implementation(devNpm("sass", "^1.52.1"))

    testImplementation(kotlin("test"))
}

licensee {
    allow("Apache-2.0")
}
