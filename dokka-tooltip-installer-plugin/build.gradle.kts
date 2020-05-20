import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `maven-publish`
    id("java")
    kotlin("jvm")
}

group = "org.jetbrains.dokka.plugins.tooltipInstaller"
version = "1.0-SNAPSHOT"


repositories {
    jcenter()
    mavenCentral()
    mavenLocal()
    maven("https://dl.bintray.com/kotlin/kotlin-dev")
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
}

dependencies {
    compileOnly("org.jetbrains.dokka", "dokka-core", "0.11.0-SNAPSHOT")
    compileOnly("org.jetbrains.dokka", "dokka-base", "0.11.0-SNAPSHOT")
    implementation("com.google.code.gson:gson:2.8.5")
    implementation(kotlin("stdlib-jdk8"))
}

val sourceJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

publishing {
    publications {
        register<MavenPublication>("tooltip-installer-plugin") {
            groupId = "org.virtuslab.dokka.plugins"
            artifactId = "tooltip-installer-plugin"
            version = "0.1-SNAPSHOT"

            from(components["java"])
            artifact(sourceJar.get())
        }
    }
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}