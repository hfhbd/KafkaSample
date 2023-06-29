plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.plugins.kotlin.multiplatform.toDep())
    implementation(libs.plugins.kotlin.serialization.toDep())
    implementation(libs.plugins.jetbrains.compose.toDep())
    implementation(libs.plugins.licensee.toDep())
}

fun Provider<PluginDependency>.toDep() = map {
    "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version}"
}
