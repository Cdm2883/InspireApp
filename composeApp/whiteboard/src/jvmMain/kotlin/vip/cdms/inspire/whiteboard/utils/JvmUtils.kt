@file:Suppress("UnusedReceiverParameter")

package vip.cdms.inspire.whiteboard.utils

import vip.cdms.inspire.utils.JvmUtils
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

fun JvmUtils.copyToJavaLibraryPath(libraryFile: File) = System.getProperty("java.library.path").split(File.pathSeparator).any { libraryPath ->
    val targetDir = File(libraryPath).takeIf { it.exists() && it.isDirectory } ?: return@any false
    val targetFile = File(targetDir, libraryFile.name).apply { deleteOnExit() }
    runCatching { Files.copy(libraryFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING) }.isSuccess
}

fun JvmUtils.tempDir(prefix: String): File = Files.createTempDirectory(prefix).toFile().apply { deleteOnExit() }

fun JvmUtils.tempExtractInsideJar(fileName: String, tempDir: File = tempDir("inside_jar")): File {
    val inputStream = object {}.javaClass.getResourceAsStream("/$fileName")
        ?: throw IllegalArgumentException("File not found in JAR: $fileName")
    return File(tempDir, fileName).apply {
        deleteOnExit()
        Files.copy(inputStream, toPath(), StandardCopyOption.REPLACE_EXISTING)
    }
}
