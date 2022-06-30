import org.jetbrains.compose.*

plugins {
    kotlin("multiplatform") version "1.6.21" apply false
    kotlin("plugin.serialization") version "1.7.0" apply false
    id("org.jetbrains.compose") version "1.2.0-alpha01-dev731" apply false
}

repositories {
    mavenCentral()
    maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
}
