package vip.cdms.inspire.gradle

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.provider.Provider

fun CommonExtension<*, *, *, *, *, *>.defaultCommonInspireAndroidConfig(
    namespace: String? = null,
    compileSdk: Provider<String>,
    minSdk: Provider<String>,
) {
    this.namespace = "vip.cdms.inspire${namespace?.let { ".$it" } ?: ""}"
    this.compileSdk = compileSdk.get().toInt()
    defaultConfig {
        this.minSdk = minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
