plugins {
    kotlin("jvm")
    id("app.cash.licensee")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(projects.streaming)

    testImplementation(kotlin("test"))
}

licensee {
    allow("Apache-2.0")
    allow("MIT")
    allow("EPL-1.0")
    allow("BSD-2-Clause")
}
