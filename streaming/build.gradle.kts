plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(projects.shared)

    implementation("org.testcontainers:kafka:1.15.3")
    implementation("org.apache.kafka:kafka-streams:2.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")

    testImplementation(kotlin("test"))
}

