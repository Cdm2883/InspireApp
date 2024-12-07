import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.compose.ComposePlugin
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.compose.desktop.application.tasks.AbstractProguardTask
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import vip.cdms.inspire.gradle.HostPlatforms
import vip.cdms.inspire.gradle.buildDirFile
import vip.cdms.inspire.gradle.defaultCommonInspireAndroidConfig
import vip.cdms.inspire.gradle.transformText

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.gradleup.shadow)
}

val appVersion: String = libs.versions.inspire.app.version.get()
val appVersionCore = """(\d+)\.(\d+)\.(\d+)""".toRegex().find(appVersion)?.value ?: throw Error()
val appVersionCode = libs.versions.inspire.app.code.get().toInt()

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    jvm {
//        withJava()  // KT-30878
        compilations {
            val main by getting
            val fluent by compilations.creating {
                defaultSourceSet {
                    dependencies {
                        implementation(main.compileDependencyFiles + main.runtimeDependencyFiles + main.output.allOutputs)
                        ComposePlugin.Dependencies(project).desktop.run {
                            listOf(macos_arm64, macos_x64, linux_arm64, linux_x64, windows_x64)
                        }.forEach { runtimeOnly(it) }
                    }
                }

                val taskGroup = "compose desktop fluent"
                val codeMainClass = "vip.cdms.inspire.fluent.MainKt"
                val codeSource = compileDependencyFiles + runtimeDependencyFiles + output.allOutputs
                tasks.register<JavaExec>("runFluentDesktop") {
                    group = taskGroup
                    mainClass = codeMainClass
                    classpath = codeSource
                }
                val jarTask = tasks.register<ShadowJar>("packageFluentDesktopForCurrentOs") {
                    group = taskGroup
                    archiveBaseName = "Inspire-fluent-" + HostPlatforms.Current.kebabName
                    archiveVersion = appVersion
                    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
                    from(codeSource) {
                        val skikoExcludes = enumValues<HostPlatforms>().associateWith { "**skiko**${it.kebabName}**" }
                        exclude(skikoExcludes.filter { (key) -> key != HostPlatforms.Current }.values)
                    }
                    manifest {
                        attributes["Main-Class"] = codeMainClass
                    }
                }
                tasks.register<AbstractProguardTask>("packageReleaseFluentDesktopForCurrentOs") {
                    group = taskGroup
                    dependsOn(jarTask)
                    val jar = jarTask.get()

                    proguardVersion = libs.versions.proguard.get()
                    proguardFiles.from(project.configurations.detachedConfiguration(
                        project.dependencies.create(libs.plugins.proguard.gradle.get().toString())
                    ))

                    mainClass = codeMainClass
                    mainJar = jar.archiveFile
                    joinOutputJars = true
                    inputFiles.from(jar.archiveFile)
                    destinationDir = buildDirFile("compose/jars")

                    defaultComposeRulesFile = buildDirFile("compose/default-resources/${libs.versions.compose.multiplatform.get()}/default-compose-desktop-rules.pro")
                    configurationFiles.from(
                        buildDirFile("compose/tmp/proguardReleaseJars/jars-config.pro"),
                        project.file("compose-desktop.pro"),
                        project.file("compose-desktop-fluent.pro")
                    )
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
            implementation(projects.composeApp.whiteboard)
            implementation(projects.shared)
            implementation(projects.dataSynchronizer)
            implementation(libs.jetbrains.androidx.lifecycle.viewmodel.compose)  // do not change, decompose is so huge
            implementation(libs.jetbrains.androidx.navigation.compose)           // and not fully adapted to official libs.
        }

        // Material Design
        val materialMain by creating {
            dependsOn(commonMain.get())
            dependencies {
                implementation(compose.material3)
            }
        }
        androidMain {
            dependsOn(materialMain)
            dependencies {
                implementation(compose.preview)
                implementation(libs.androidx.activity.compose)
            }
        }

        val webMain by creating {
            dependsOn(materialMain)
            dependencies {
                implementation(libs.kilua.dom)
            }
        }
        wasmJsMain {
            dependsOn(webMain)
        }
        jsMain {
            dependsOn(webMain)
        }

        // Now is for JVM only, but will be extended
        // after Compose Desktop for macOS & Windows **native** compilation.
        // So put JVM only dependencies here is **NOT** allowed.
        val desktopMain by creating {
            dependsOn(commonMain.get())
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
        jvmMain {
            dependsOn(desktopMain)
            dependsOn(materialMain)
            dependencies {
                implementation(libs.kotlinx.coroutines.swing)
            }
        }
        // Fluent Design
        val jvmFluent by getting {
            dependencies {
                compileOnly(projects.composeApp)  // fix IDEA Auto Completing
                implementation(libs.konyaco.fluent)
                implementation(libs.konyaco.fluent.icons.extended)
                implementation(libs.mayakapps.compose.window.styler)
            }
        }
    }
}

android {
    defaultCommonInspireAndroidConfig(
        compileSdk = libs.versions.android.sdk.compile,
        minSdk = libs.versions.android.sdk.min
    )
    defaultConfig {
        applicationId = "vip.cdms.inspire"
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
        val debugResources = listOf(
            "kotlin/**.kotlin_builtins",  // https://stackoverflow.com/questions/41052868/what-are-kotlin-builtins-files-and-can-i-omit-them-from-my-uberjars
            "META-INF/**.version",
            "DebugProbesKt.bin",  // https://github.com/Kotlin/kotlinx.coroutines?tab=readme-ov-file#avoiding-including-the-debug-infrastructure-in-the-resulting-apk
        )
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-android-rules.pro")
            packaging  {
                resources.excludes += debugResources
                resources.excludes += "kotlin-tooling-metadata.json"  // https://github.com/Kotlin/kotlinx.coroutines/issues/3158#issuecomment-1023151105
            }
        }
        create("preview") {
            initWith(getByName("release"))
            versionNameSuffix = "-preview"
            applicationIdSuffix = ".preview"
        }
        debug {
            versionNameSuffix = "-debug"
            applicationIdSuffix = ".debug"
            packaging  {
                resources.excludes -= debugResources
            }
        }
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

// Web Resources
val webResourcesInject = mapOf<_, File.(isWasm: Boolean) -> Unit>(
    "index.html" to { transformText {
        replace("{% TARGET_HEAD %}",
            if (it) "" else """
                <script src="skiko.js"></script>
            """.trimIndent()
        )
    } }
)
fun injectWebResources(isWasm: Boolean) = webResourcesInject.forEach { (path, action) ->
    val file = buildDirFile("processedResources/${if (isWasm) "wasmJs" else "js"}/main/$path")
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
            version = libs.versions.proguard.get()
            obfuscate = true
            optimize = true
            joinOutputJars = true
            configurationFiles.from(project.file("compose-desktop.pro"))
        }
    }
}
