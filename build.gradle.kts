plugins {
    java
    id("org.springframework.boot") version "3.1.5"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "ru.project"
version = "0.0.1-SNAPSHOT"
description = "career-test"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.postgresql:postgresql")
    implementation("com.google.zxing:core:3.5.2")
    implementation("com.google.zxing:javase:3.5.2")

    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// Это важно для Railway - создает исполняемый JAR
tasks.bootJar {
    archiveFileName.set("career-test.jar")
}