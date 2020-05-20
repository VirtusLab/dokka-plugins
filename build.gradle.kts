buildscript {
    repositories {
        maven("https://dl.bintray.com/kotlin/kotlin-dev")
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
    }
}

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.4-M2-eap-70"
}

group = "org.jetbrains.dokka.plugins"
version = "1.0-SNAPSHOT"


repositories {
    jcenter()
    mavenCentral()
    mavenLocal()
    maven("https://dl.bintray.com/kotlin/kotlin-dev")
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
}

val kotlin_version: String = "1.4-M2-eap-70"
dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlin_version")
}