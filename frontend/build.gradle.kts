plugins {
    kotlin("js")
    id("org.jetbrains.compose")
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
    val ktor = "2.1.1"
    implementation("io.ktor:ktor-client-core:$ktor")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor")
    implementation("io.ktor:ktor-client-resources:$ktor")
    implementation(projects.shared)

    implementation(compose.web.core)
    implementation("app.softwork:bootstrap-compose:0.1.10")
    implementation(devNpm("sass-loader", "^13.0.0"))
    implementation(devNpm("sass", "^1.52.1"))

    testImplementation(kotlin("test"))
}
