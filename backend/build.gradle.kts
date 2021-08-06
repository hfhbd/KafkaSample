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
    implementation("org.apache.kafka:kafka-clients:2.8.0")
    implementation("io.ktor:ktor-server-cio:1.6.2")

    testImplementation(kotlin("test"))
}
