import org.jetbrains.compose.*

plugins {
    kotlin("multiplatform") version "1.6.21" apply false
    kotlin("plugin.serialization") version "1.6.21" apply false
    id("org.jetbrains.compose") version "0.0.0-on-rebase-12-apr-2022-dev670" apply false
}

repositories {
    mavenCentral()
    jetbrainsCompose()
}
