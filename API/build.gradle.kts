val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "1.8.10"
    id("io.ktor.plugin") version "2.2.4"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.10"
}

kotlin {
    jvmToolchain(jdkVersion = 17)
}

group = "de.matthiasklenz"
version = "0.0.1"
application {
    mainClass.set("de.matthiasklenz.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(
        "ch.qos.logback:logback-classic:$logback_version"
    )
    implementation("io.ktor:ktor-server-core-jvm:2.3.0")
    implementation("io.ktor:ktor-server-auth-jvm:2.3.0")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:2.3.0")
    implementation("io.ktor:ktor-client-core-jvm:2.3.0")
    implementation("io.ktor:ktor-client-apache-jvm:2.3.0")
    implementation("io.ktor:ktor-server-host-common-jvm:2.3.0")
    implementation("io.ktor:ktor-server-cors-jvm:2.3.0")
    implementation(
        "io.ktor:ktor-server-content-negotiation-jvm:2.3.0"
    )
    implementation(
        "io.ktor:ktor-serialization-kotlinx-json-jvm:2.3.0"
    )
    implementation("io.ktor:ktor-server-netty-jvm:2.3.0")
    testImplementation(
        "org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version"
    )

    // database
    implementation("org.ktorm:ktorm-core:3.5.0")
    implementation(
        group = "org.ktorm",
        name = "ktorm-support-mysql",
        version = "3.4.1"
    )
    implementation("mysql:mysql-connector-java:8.0.30")

    // cache
    implementation("com.google.guava:guava:31.1-jre")

    // koin di
    implementation("io.insert-koin:koin-core:3.2.2")
    testImplementation("io.insert-koin:koin-test:3.2.2")

    // config
    implementation("com.charleskorn.kaml:kaml:0.49.0")
    implementation(
        "org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1"
    )
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation("io.ktor:ktor-server-tests-jvm:2.3.0")
}
