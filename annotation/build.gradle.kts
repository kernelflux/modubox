plugins {
    kotlin("jvm")
    id("java-library")
    id("maven-publish")
}

group = "com.kernelflux.modubox"
version = "1.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // 如果需要其他依赖，在这里添加
}
