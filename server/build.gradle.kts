plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlinSerialization)
    application
}

group = "ru.nofeature.hackathon"
version = "1.0.0"
application {
    mainClass.set("ru.nofeature.hackathon.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverNetty)
    implementation(libs.ktor.serverJson)
    implementation(libs.ktor.server.cors)

    testImplementation(libs.ktor.serverTestHost)
    testImplementation(libs.kotlin.testJunit)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.0")
    implementation("org.xerial:sqlite-jdbc:3.49.1.0")
    implementation(libs.serialization)
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation("com.h2database:h2:2.2.224")
}