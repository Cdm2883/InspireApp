import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import vip.cdms.inspire.gradle.*

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.adamko.dokkatoo.html)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    val jPen = object {
        private val libs = "src/jvmMain/libs/jpen"
        val jar = "$libs/jpen-2.jar"
        val jni = object {
//            val linux = "$libs/libjpen-2-4.so"
            val linux_64 = "$libs/libjpen-2-4-x86_64.so"
//            val win = "$libs/jpen-2-3.dll"
            val win_64 = "$libs/jpen-2-3-64.dll"
            val osx = "$libs/libjpen-2-3.jnilib"
//            val all = arrayOf(linux, linux_64, win, win_64, osx)
        }
    }
    jvm {
        compilations {
            val main by getting {
                tasks.named("jvmProcessResources", doLast {
                    val jPenRes = buildDirFile("processedResources/jvm/main/jpen").apply { deleteRecursively() }
                    val jPenJni = when (HostPlatforms.Current) {
                        HostPlatforms.MacosArm64 -> jPen.jni.osx
                        HostPlatforms.LinuxX64 -> jPen.jni.linux_64
                        HostPlatforms.WindowsX64 -> jPen.jni.win_64
                        else -> null
                    }?.let(::file) ?: return@doLast
                    jPenRes.mkdirs()
                    jPenJni.copyTo(File(jPenRes, jPenJni.name), overwrite = true)
                })
            }
        }
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
            implementation(compose.ui)
            implementation(compose.foundation)
            implementation(compose.components.resources)
            implementation(projects.shared)
            implementation(projects.dataSynchronizer)
        }

        jvmMain {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.kotlinx.coroutines.swing)
                implementation(files(jPen.jar))
            }
        }
    }
}

android.defaultCommonInspireAndroidConfig(
    namespace = "whiteboard",
    compileSdk = libs.versions.android.sdk.compile,
    minSdk = libs.versions.android.sdk.min
)

compose.desktop {
    application {
        mainClass = "vip.cdms.inspire.whiteboard.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Inspire Whiteboard"
            packageVersion = """(\d+)\.(\d+)\.(\d+)""".toRegex().find(libs.versions.inspire.app.version.get())?.value ?: error("")
        }

        buildTypes.release.proguard {
            version = libs.versions.proguard.get()
            obfuscate = true
            optimize = true
            joinOutputJars = true
            configurationFiles.from(project.file("../compose-desktop.pro"))
        }
    }
}
