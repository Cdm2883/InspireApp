import dev.adamko.dokkatoo.tasks.DokkatooGenerateTask
import vip.cdms.inspire.gradle.footerCopyright

plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.inspire.gradle.plugin)
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlinx.serialization) apply false
    alias(libs.plugins.gradleup.shadow) apply false
    alias(libs.plugins.adamko.dokkatoo.html)
}

version = libs.versions.inspire.app.version.get()

subprojects {
    plugins.apply(rootProject.libs.plugins.inspire.gradle.plugin.get().pluginId)
}

dependencies {
    dokkatoo(projects.dataSynchronizer)
    dokkatoo(projects.composeApp.whiteboard)
}

val dokkaInjectJs = """
    document.querySelectorAll(".platform-tag.common-like").forEach(tag => {
        const platform = tag.innerText.toLowerCase()
        if (platform.includes("web")) tag.classList.replace("common-like", "wasm-like")
    })
""".trimIndent()
tasks.register("generateKDoc") {
    group = "dokkatoo"
    dependsOn(tasks.dokkatooGenerate)
    doLast { layout.buildDirectory
        .file("dokka/html/scripts/navigation-loader.js")
        .get().asFile.appendText(dokkaInjectJs) }
}
dokkatoo {
    footerCopyright()
}
tasks.withType<DokkatooGenerateTask> {
    generator.includes.from("dokka-homepage.md")
}
