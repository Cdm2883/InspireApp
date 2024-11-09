package vip.cdms.inspire.gradle

import org.gradle.api.Project

fun Project.buildDirFile(path: String) = layout.buildDirectory.file(path).get().asFile
