pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    // This plugin allows Gradle to auto-download JDK 17 since you don't have it installed
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "SentinelFlow"
include(":SentinelApp")