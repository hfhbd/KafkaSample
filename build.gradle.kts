import org.jetbrains.compose.*

plugins {
    kotlin("multiplatform") version "1.7.10" apply false
    kotlin("plugin.serialization") version "1.7.10" apply false
    id("org.jetbrains.compose") version "1.2.0-alpha01-dev745" apply false
}

repositories {
    mavenCentral()
    maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
}
