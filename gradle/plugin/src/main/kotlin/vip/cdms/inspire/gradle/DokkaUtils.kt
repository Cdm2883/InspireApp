package vip.cdms.inspire.gradle

import dev.adamko.dokkatoo.DokkatooExtension
import dev.adamko.dokkatoo.dokka.parameters.DokkaSourceSetSpec
import dev.adamko.dokkatoo.dokka.plugins.DokkaHtmlPluginParameters
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import java.io.File
import java.net.URI
import java.time.LocalDate

fun DokkaSourceSetSpec.sourceLinkToGithub(project: Project) {
    sourceLink {
        localDirectory.set(project.file("src/$name/kotlin"))
        val pathFromRepoRoot = project.projectDir.toRelativeString(project.veryRootProject.projectDir).replace(File.separator, "/")
        val remoteUri = "${Constant.GITHUB_REPO}/tree/master/$pathFromRepoRoot/src/$name/kotlin"
        remoteUrl.set(URI(remoteUri))
        remoteLineSuffix.set("#L")
    }
}

fun DokkatooExtension.footerCopyright() {
    (pluginsConfiguration as ExtensionAware).extensions.configure<DokkaHtmlPluginParameters>("html") {
        footerMessage.set("Copyright © 2024 - ${LocalDate.now().year} Cdm2883")
    }
}
