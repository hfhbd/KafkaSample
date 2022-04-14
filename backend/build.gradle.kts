plugins {
    kotlin("jvm")
    application
}

repositories {
    mavenCentral()
}

application {
    mainClass.set("ServerKt")
}

dependencies {
    implementation(projects.shared)
    implementation("org.apache.kafka:kafka-clients:3.1.0")
    implementation("io.ktor:ktor-server-cio:2.0.0")

    testImplementation(kotlin("test"))
}
