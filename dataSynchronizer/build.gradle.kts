import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import vip.cdms.inspire.gradle.defaultCommonInspireAndroidConfig
import vip.cdms.inspire.gradle.footerCopyright
import vip.cdms.inspire.gradle.sourceLinkToGithub

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.adamko.dokkatoo.html)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    js {
        browser()
    }
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(libs.kotlinx.serialization.json)
        }

        androidMain.dependencies {
            implementation(libs.androidx.appcompat)
        }

        val webMain by creating {
            dependsOn(commonMain.get())
            dependencies {
                implementation(libs.kilua.dom)
                implementation(npm("webrtc-adapter", "9.0.1"))
            }
        }
        wasmJsMain {
            dependsOn(webMain)
        }
        jsMain {
            dependsOn(webMain)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.uiTest)
        }
        val jvmTest by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
    }

    compilerOptions {
        optIn.addAll(
            "androidx.compose.ui.test.ExperimentalTestApi",
        )
        freeCompilerArgs.addAll(
            "-Xexpect-actual-classes",
        )
    }
}

android.defaultCommonInspireAndroidConfig(
    namespace = "sync",
    compileSdk = libs.versions.android.sdk.compile,
    minSdk = libs.versions.android.sdk.min
)

dokkatoo {
    moduleName.set("Data Synchronizer")
    dokkatooSourceSets.configureEach {
        includes.from("dokka-module.md")
        sourceLinkToGithub(project)
    }
    footerCopyright()
}
