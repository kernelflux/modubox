import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.ksp)
    id("com.kernelflux.android.module")
}


val keystorePropsFile = rootProject.file("keystore.properties")
val keystoreProps = Properties()
if (keystorePropsFile.exists()) {
    keystoreProps.load(keystorePropsFile.inputStream())
}
// helper to read env if property missing
fun propOrEnv(key: String): String? =
    (keystoreProps.getProperty(key) ?: System.getenv(key))?.takeIf { it.isNotBlank() }

android{
    namespace = "com.kernelflux.moduboxsample"

    signingConfigs {
        create("release") {
            storeFile = file("sample.jks")
            storePassword = "123456"
            keyAlias = "key0"
            keyPassword = "123456"
        }
    }


    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // 明确指定所有库模块使用release变体
    implementation(project(":core-impl")) {
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
    implementation(project(":feature-login")) {
        attributes {
            attribute(com.android.build.api.attributes.BuildTypeAttr.ATTRIBUTE, objects.named("release"))
        }
    }
    
    // KSP注解处理器 - Java库不需要变体选择
    ksp(project(":annotation-processor"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    

}