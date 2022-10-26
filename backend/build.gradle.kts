import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

buildscript {
    dependencies { classpath("com.nbottarini:asimov-environment:2.0.0") }
}

group = "com.ldamore.backend"
version = rootProject.file("VERSION").readText().trim()

repositories { mavenCentral() }

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.nbottarini:asimov-environment:2.0.0")
    implementation("org.slf4j:slf4j-simple:1.7.36")
    implementation("io.javalin:javalin:4.6.4")
}

tasks.withType<KotlinCompile> { kotlinOptions.jvmTarget = "16" }

kotlin {
    sourceSets["main"].apply {
        kotlin.srcDirs("src", "generated")
        resources.srcDirs("resources")
    }
    sourceSets["test"].apply {
        kotlin.srcDir("test")
        resources.srcDir("test_resources")
    }
}

java {
    sourceSets["main"].apply {
        java.srcDirs("src", "generated")
        resources.srcDirs("resources")
    }
    sourceSets["test"].apply {
        java.srcDir("test")
        resources.srcDir("test_resources")
    }
}

tasks.getByName<ShadowJar>("shadowJar") {
    archiveFileName.set("backend_api.jar")
    manifest {
        attributes(mapOf(
            "Main-Class" to "com.ldamore.backend.HttpMainKt",
            "VERSION" to project.version
        ))
    }
}