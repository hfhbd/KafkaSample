plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(projects.shared)

    implementation("org.testcontainers:kafka:1.17.2")
    implementation("org.apache.kafka:kafka-streams:3.2.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")

    testImplementation(kotlin("test"))
}

