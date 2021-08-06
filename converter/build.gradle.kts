plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(projects.shared)
    testImplementation(kotlin("test"))
}
