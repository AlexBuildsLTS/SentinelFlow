import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// ==================================================================
// 1. MASTER PLUGIN DECLARATIONS
// ==================================================================
plugins {
    // Android Plugins
    id("com.android.application") version "8.7.0" apply false
    id("com.android.library") version "8.7.0" apply false
    // ✅ UPGRADED to 2.3.0 to match your library metadata
    id("org.jetbrains.kotlin.android") version "2.3.0" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.3.0" apply false

    // Backend / Spring Boot Plugins
    id("org.springframework.boot") version "3.4.0" // ⚡ Upgraded for Kotlin 2.3.0 synergy
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("jvm") version "2.3.0"
    kotlin("plugin.spring") version "2.3.0"
    kotlin("plugin.serialization") version "2.3.0"



}

// ==================================================================
// 2. PROJECT METADATA
// ==================================================================
group = "com.sentinel.app"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

// ==================================================================
// 3. BACKEND DEPENDENCIES (SPRING BOOT)
// ==================================================================
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("io.jsonwebtoken:jjwt-api:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.5")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.postgresql:r2dbc-postgresql")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        // ✅ NEW SYNTAX for Kotlin 2.3.0
        freeCompilerArgs.add("-Xjsr305=strict")
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
    }
}
tasks.withType<Test> {
    useJUnitPlatform()
}