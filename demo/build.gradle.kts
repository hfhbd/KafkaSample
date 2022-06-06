plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(projects.shared)

    implementation(projects.streaming)
    implementation(projects.mocker)
    implementation(projects.converter)
    implementation(projects.backend)
}
