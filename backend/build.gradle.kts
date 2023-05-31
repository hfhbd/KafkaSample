plugins {
    kotlin("jvm")
    application
    id("app.cash.licensee")
}

application {
    mainClass.set("ServerKt")
}

kotlin.jvmToolchain(11)

dependencies {
    implementation(projects.streaming)
    implementation("org.apache.kafka:kafka-clients:3.4.0")

    val ktor = "2.3.1"
    implementation("io.ktor:ktor-server-cio:$ktor")
    implementation("io.ktor:ktor-server-resources:$ktor")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor")
    implementation("io.ktor:ktor-server-cors:$ktor")

    testImplementation(kotlin("test"))
}

licensee {
    allow("Apache-2.0")
    allow("MIT")
    allow("EPL-1.0")
    allow("BSD-2-Clause")
}
