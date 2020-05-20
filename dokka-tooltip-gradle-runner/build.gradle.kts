//import org.jetbrains.configureBintrayPublication

buildscript {
//    dependencies {
//        classpath("com.gradle.publish:plugin-publish-plugin")
//    }

    repositories {
        jcenter()
        mavenCentral()
        mavenLocal()
        maven(url = "https://dl.bintray.com/jetbrains/markdown/")
        maven(url = "https://plugins.gradle.org/m2/")
        maven("https://dl.bintray.com/kotlin/kotlin-dev")
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
    }
}

plugins {
    `maven-publish`
    id("java")
    kotlin("jvm")
}

repositories {
    jcenter()
    mavenCentral()
    mavenLocal()
    maven(url = "https://dl.bintray.com/jetbrains/markdown/")
    maven(url = "https://plugins.gradle.org/m2/")
    google()
    maven("https://dl.bintray.com/kotlin/kotlin-dev")
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
}

dependencies {
    val kotlin_version: String by project

//    implementation(project(":dokka-tooltip-generator-plugin"))
//    implementation(project(":dokka-tooltip-installer-plugin"))
    compileOnly("org.jetbrains.dokka", "dokka-core", "0.11.0-SNAPSHOT")
    compileOnly("org.jetbrains.dokka:dokka-gradle-plugin:0.11.0-SNAPSHOT")
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:${kotlin_version}")
    compileOnly("com.android.tools.build:gradle:3.0.0")
    compileOnly("com.android.tools.build:gradle-core:3.0.0")
    compileOnly("com.android.tools.build:builder-model:3.0.0")
    compileOnly(gradleApi())
    compileOnly(gradleKotlinDsl())
    constraints {
        compileOnly("org.jetbrains.kotlin:kotlin-reflect:${kotlin_version}") {
            because("kotlin-gradle-plugin and :core both depend on this")
        }
    }
}

val sourceJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

publishing {
    publications {
        register<MavenPublication>("dokkaTooltipGradleRunner") {
            groupId = "org.virtuslab.dokka.gradle"
            artifactId = "dokka-tooltip-gradle-runner"
            version = "0.1-SNAPSHOT"

            from(components["java"])
            artifact(sourceJar.get())
        }
    }
}