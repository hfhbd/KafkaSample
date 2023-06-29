plugins {
    kotlin("jvm")
    id("application")
    id("app.cash.licensee")
}

application {
    mainClass.set("ServerKt")
}

kotlin.jvmToolchain(11)

dependencies {
    implementation(projects.streaming)
    implementation(libs.kafka.clients)

    implementation(libs.ktor.server.cio)
    implementation(libs.ktor.server.resources)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.cors)

    testImplementation(kotlin("test"))
}

licensee {
    allow("Apache-2.0")
    allow("MIT")
    allow("EPL-1.0")
    allow("BSD-2-Clause")
}
