plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(projects.shared)

    implementation("org.apache.kafka:kafka-clients:2.8.0")

    testImplementation(kotlin("test"))
}
