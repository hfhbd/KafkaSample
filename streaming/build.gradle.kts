plugins {
    kotlin("jvm")
    id("app.cash.licensee")
}

kotlin.jvmToolchain(11)

dependencies {
    api(projects.shared)

    implementation("org.testcontainers:kafka:1.18.3")
    implementation("org.apache.kafka:kafka-streams:3.5.0")

    testImplementation(kotlin("test"))
}

licensee {
    allow("Apache-2.0")
    allow("MIT")
    allow("EPL-1.0")
    allow("BSD-2-Clause")
}
