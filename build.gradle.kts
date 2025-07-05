plugins {
    kotlin("multiplatform") version "2.2.0"
    kotlin("plugin.serialization") version "2.2.0"
}

kotlin {
    jvm()
}

dependencies {
    commonMainImplementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.9.0")
}

