import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import vip.cdms.inspire.gradle.defaultCommonInspireAndroidConfig

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    androidTarget()
    
    jvm()

    js {
        browser()
    }
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }
    
    sourceSets {
        commonMain.dependencies {
            api(libs.kotlinx.serialization.json)
        }
    }
}

android.defaultCommonInspireAndroidConfig(
    namespace = "shared",
    compileSdk = libs.versions.android.sdk.compile,
    minSdk = libs.versions.android.sdk.min
)
