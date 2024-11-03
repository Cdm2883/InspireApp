import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.compose.ComposePlugin
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.gradleup.shadow)
}

val appVersion = libs.versions.inspire.app.version.get()
val appVersionCore = """(\d+)\.(\d+)\.(\d+)""".toRegex().find(appVersion)?.value ?: throw Error()
val appVersionCode = libs.versions.inspire.app.code.get().toInt()

enum class DesktopPlatforms(val kebabName: String) {
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
                else -> throw GradleException("Host OS is not supported in Compose Desktop.")
            }
        }
    }
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    jvm {
        compilations {
            val main by getting
            val fluent by compilations.creating {
                defaultSourceSet {
                    dependencies {
                        implementation(main.compileDependencyFiles + main.runtimeDependencyFiles + main.output.allOutputs)
                        ComposePlugin.Dependencies(project).desktop.run {
                            runtimeOnly(macos_arm64)
                            runtimeOnly(macos_x64)
                            runtimeOnly(linux_arm64)
                            runtimeOnly(linux_x64)
                            runtimeOnly(windows_x64)
                        }
                    }
                }

                val group = "compose desktop fluent"
                val codeSource = compileDependencyFiles + runtimeDependencyFiles + output.allOutputs
                tasks.register<JavaExec>("runFluentDesktop") {
                    setGroup(group)
                    mainClass = "vip.cdms.inspire.fluent.MainKt"
                    classpath = codeSource
                }
                tasks.register<ShadowJar>("packageFluentDesktopForCurrentOs") {
                    setGroup(group)
                    archiveBaseName = "Inspire-fluent-" + DesktopPlatforms.Current.kebabName
                    archiveVersion = appVersion
                    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
                    from(codeSource) {
                        val skikoExcludes = mutableMapOf(
                            DesktopPlatforms.MacosArm64 to "**skiko**macos-arm64**",
                            DesktopPlatforms.MacosX64 to "**skiko**macos-x64**",
                            DesktopPlatforms.LinuxArm64 to "**skiko**linux-arm64**",
                            DesktopPlatforms.LinuxX64 to "**skiko**linux-x64**",
                            DesktopPlatforms.WindowsX64 to "**skiko**windows-x64**",
                        )
                        exclude(skikoExcludes.filter { (key) -> key != DesktopPlatforms.Current }.values)
                    }
                    manifest {
                        attributes["Main-Class"] = "vip.cdms.inspire.fluent.MainKt"
                    }
                }
                tasks.register("packageReleaseFluentDesktopForCurrentOs") {
                    // TODO: ProGuard
                    // TODO: Github Actions auto building
                }
            }
        }
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "composeApp"
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
            }
        }
        binaries.executable()
    }
    js(IR) {
        moduleName = "composeApp"
        browser {
            commonWebpackConfig {
                outputFileName = "composeApp.js"
            }
        }
        binaries.executable()
    }

    sourceSets {
        //                        ┌──────────┐
        //                        │commonMain│
        //   <SourceSets>         └─────┬────┘
        //                    ┌─────────┴───────┐
        //             ┌──────▼─────┐     ┌─────▼─────┐
        //             │materialMain│     │desktopMain│
        //             └──────┬─────┘     └─────┬─────┘
        //       ┌────────────┼────────┐   ┌────┴────┐
        // ┌─────▼─────┐  ┌───▼───┐  ┌─▼───▼─┐  ┌────▼────┐
        // │androidMain│  │webMain│  │jvmMain│  │jvmFluent│
        // └───────────┘  └───┬───┘  └───────┘  └─────────┘
        //              ┌─────┴──────┐
        //         ┌────▼─────┐  ┌───▼──┐
        //         │wasmJsMain│  │jsMain│
        //         └──────────┘  └──────┘

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.ui)
            implementation(compose.foundation)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(projects.shared)
        }

        // Material Design
        val materialMain by creating {
            dependsOn(commonMain.get())
            dependencies {
                implementation(compose.material3)
            }
        }
        androidMain.get().apply {
            dependsOn(materialMain)
            dependencies {
                implementation(compose.preview)
                implementation(libs.androidx.activity.compose)
            }
        }

        val webMain by creating {
            dependsOn(materialMain)
        }
        wasmJsMain.get().apply {
            dependsOn(webMain)
        }
        jsMain.get().apply {
            dependsOn(webMain)
        }

        val desktopMain by creating {
            dependsOn(commonMain.get())
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
        jvmMain.get().apply {
            dependsOn(desktopMain)
            dependsOn(materialMain)
        }
        // Fluent Design
        val jvmFluent by getting {
            dependencies {
                implementation(libs.konyaco.fluent)
                implementation(libs.konyaco.fluent.icons.extended)
                implementation(libs.mayakapps.compose.window.styler)
            }
        }
    }
}

android {
    namespace = "vip.cdms.inspire"
    compileSdk = libs.versions.android.sdk.compile.get().toInt()

    defaultConfig {
        applicationId = "vip.cdms.inspire"
        minSdk = libs.versions.android.sdk.min.get().toInt()
        targetSdk = libs.versions.android.sdk.target.get().toInt()
        versionCode = appVersionCode
        versionName = appVersion
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-android-rules.pro")
        }
        create("preview") {
            initWith(getByName("release"))
            versionNameSuffix = "-preview"
            applicationIdSuffix = ".preview"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

// Web Resources
val webResourcesInject = mapOf<String, File.(isWasm: Boolean) -> Unit>(
    "index.html" to {
        writeText(readText().replace("{% TARGET_HEAD %}", if (it) "" else """
            <script src="skiko.js"></script>
        """.trimIndent()))
    }
)
fun injectWebResources(isWasm: Boolean) = webResourcesInject.forEach { (path, action) ->
    val processedDir = "${layout.buildDirectory.get()}/processedResources/${if (isWasm) "wasmJs" else "js"}/main"
    val file = file("$processedDir/$path")
    action(file, isWasm)
}
tasks.named("wasmJsProcessResources") {
    doLast { injectWebResources(true) }
}
tasks.named("jsProcessResources") {
    doLast { injectWebResources(false) }
}

// DO NOT DELETE or FIX IT: build
tasks {
    named("jsBrowserProductionWebpack") {
        dependsOn("wasmJsProductionExecutableCompileSync")
    }
    named("wasmJsBrowserProductionWebpack") {
        dependsOn("jsProductionExecutableCompileSync")
    }
}

compose.resources {
    publicResClass = true
}

compose.desktop {
    application {
        mainClass = "vip.cdms.inspire.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Inspire"
            packageVersion = appVersionCore
        }

        buildTypes.release.proguard {
            version.set("7.5.0")
            obfuscate.set(true)
            optimize.set(true)
            joinOutputJars.set(true)
            configurationFiles.from(project.file("compose-desktop.pro"))
        }
    }
}
