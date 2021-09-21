plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(projects.shared)

    implementation("org.apache.kafka:kafka-clients:3.0.0")

    testImplementation(kotlin("test"))
}
