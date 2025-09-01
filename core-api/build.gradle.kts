plugins {
    alias(libs.plugins.android.library)
    id("com.kernelflux.android.module")
}

android {
    namespace = "com.kernelflux.modubox.core.api"

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    
    // 禁用debug变体
    variantFilter {
        if (name == "debug") {
            ignore = true
        }
    }
}

dependencies {
}

