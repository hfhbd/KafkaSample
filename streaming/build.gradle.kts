plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(projects.shared)

    implementation("org.testcontainers:kafka:1.17.3")
    implementation("org.apache.kafka:kafka-streams:3.2.1")

    testImplementation(kotlin("test"))
}
