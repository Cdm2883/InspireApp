package vip.cdms.inspire.gradle

import org.gradle.api.GradleException
import org.gradle.kotlin.dsl.provideDelegate

enum class HostPlatforms(val kebabName: String) {
    MacosArm64("macos-arm64"),
    MacosX64("macos-x64"),
    LinuxArm64("linux-arm64"),
    LinuxX64("linux-x64"),
    WindowsX64("windows-x64");
    companion object {
        val Current by lazy {
            val hostOs = System.getProperty("os.name")
            val isArm64 = System.getProperty("os.arch") == "aarch64"
            val isMingwX64 = hostOs.startsWith("Windows")
            when {
                hostOs == "Mac OS X" && isArm64 -> MacosArm64
                hostOs == "Mac OS X" && !isArm64 -> MacosX64
                hostOs == "Linux" && isArm64 -> LinuxArm64
                hostOs == "Linux" && !isArm64 -> LinuxX64
                isMingwX64 -> WindowsX64
                else -> throw GradleException("Host OS is not supported.")
            }
        }
    }
}
