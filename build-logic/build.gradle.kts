plugins {
    `kotlin-dsl`
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    //noinspection UseTomlInstead
    compileOnly("com.android.tools.build:gradle:8.12.2")
    compileOnly(kotlin("gradle-plugin"))
}

gradlePlugin {
    plugins {
        val androidConfigPlugin = this.create("androidConventionConfig")
        androidConfigPlugin.id = "com.kernelflux.android.module"
        androidConfigPlugin.implementationClass =
            "com.kernelflux.android.AndroidModuleConventionPlugin"
    }
}