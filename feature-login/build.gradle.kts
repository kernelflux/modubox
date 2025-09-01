plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.ksp)
    id("com.kernelflux.android.module")
}

android {
    namespace = "com.kernelflux.modubox.feature.login"
}

dependencies {
    // 明确指定所有库模块使用release变体
    implementation(project(":core-api")) {
        attributes {
            attribute(com.android.build.api.attributes.BuildTypeAttr.ATTRIBUTE, objects.named("release"))
        }
    }
    implementation(project(":plugin-api")) {
        attributes {
            attribute(com.android.build.api.attributes.BuildTypeAttr.ATTRIBUTE, objects.named("release"))
        }
    }
    implementation(project(":annotation"))
    
    // KSP注解处理器 - Java库不需要变体选择
    ksp(project(":annotation-processor"))
}


