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

subprojects {
    plugins.apply(rootProject.libs.plugins.inspire.gradle.plugin.get().pluginId)
}

dependencies {
    dokkatoo(projects.dataSynchronizer)
}

val dokkaInjectJs = """
    document.querySelectorAll(".platform-tag.common-like").forEach(tag => {
        const platform = tag.innerText.toLowerCase()
        if (platform.includes("web")) {
            tag.classList.remove("common-like")
            tag.classList.add("wasm-like")
        }
    })
""".trimIndent()
tasks.register("generateKDoc") {
    group = "dokkatoo"
    dependsOn(tasks.dokkatooGenerate)
    doLast {
        layout.buildDirectory
            .file("dokka/html/scripts/navigation-loader.js")
            .get().asFile.appendText(dokkaInjectJs)
    }
}
