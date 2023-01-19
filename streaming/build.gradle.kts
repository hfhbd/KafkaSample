plugins {
    kotlin("jvm")
    id("app.cash.licensee")
}

repositories {
    mavenCentral()
}

dependencies {
    api(projects.shared)

    implementation("org.testcontainers:kafka:1.17.6")
    implementation("org.apache.kafka:kafka-streams:3.3.2")

    testImplementation(kotlin("test"))
}

licensee {
    allow("Apache-2.0")
    allow("MIT")
    allow("EPL-1.0")
    allow("BSD-2-Clause")
}
