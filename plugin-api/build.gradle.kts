plugins {
    alias(libs.plugins.android.library)
    id("com.kernelflux.android.module")
}

android {
    namespace = "com.kernelflux.modubox.plugin.api"
}

dependencies {
    // 明确指定所有库模块使用release变体
    api(project(":core-api")) {
        attributes {
            attribute(com.android.build.api.attributes.BuildTypeAttr.ATTRIBUTE, objects.named("release"))
        }
    }
}

