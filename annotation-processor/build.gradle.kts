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
    implementation(project(":annotation"))
    implementation(libs.auto.service)
    implementation(libs.javapoet)
    
    // KSP依赖 - 使用硬编码版本确保兼容性
    implementation("com.google.devtools.ksp:symbol-processing-api:2.0.21-1.0.28")
    
    // Kotlin依赖
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.0.21")
}
