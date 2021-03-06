plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(projects.shared)

    implementation("org.apache.kafka:kafka-clients:3.2.1")

    testImplementation(kotlin("test"))
}
