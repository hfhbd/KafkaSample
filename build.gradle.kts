plugins {
    kotlin("multiplatform") version "1.8.20" apply false
    kotlin("plugin.serialization") version "1.8.20" apply false
    id("org.jetbrains.compose") version "1.4.0-rc03" apply false
    id("app.cash.licensee") version "1.6.0" apply false
}

repositories {
    mavenCentral()
    maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
}
