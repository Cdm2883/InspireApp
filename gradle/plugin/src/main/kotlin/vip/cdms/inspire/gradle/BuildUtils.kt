package vip.cdms.inspire.gradle

import org.gradle.api.Project
import java.io.File

fun Project.buildDirFile(path: String): File = layout.buildDirectory.file(path).get().asFile
