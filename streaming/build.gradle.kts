plugins {
    kotlin("jvm")
    id("app.cash.licensee")
}

kotlin.jvmToolchain(11)

dependencies {
    api(projects.shared)

    implementation(libs.testcontainers.kafka)
    implementation(libs.kafka.streams)

    testImplementation(kotlin("test"))
}

licensee {
    allow("Apache-2.0")
    allow("MIT")
    allow("EPL-1.0")
    allow("BSD-2-Clause")
}
