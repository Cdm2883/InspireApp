@file:Suppress("UnusedReceiverParameter")

package vip.cdms.inspire.whiteboard.utils

import vip.cdms.inspire.utils.JvmUtils
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

fun JvmUtils.copyToJavaLibraryPath(libraryFile: File) = System.getProperty("java.library.path").split(File.pathSeparator).any { libraryPath ->
    val targetDir = File(libraryPath).takeIf { it.exists() && it.isDirectory } ?: return@any false
    val targetFile = File(targetDir, libraryFile.name)
    Files.copy(libraryFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
    targetFile.deleteOnExit()
    false
}

fun JvmUtils.tempDir(prefix: String): File = Files.createTempDirectory(prefix).toFile().apply { deleteOnExit() }

fun JvmUtils.tempExtractInsideJar(
    fileName: String,
    tempDir: File = tempDir("inside_jar")
): File {
    val filePath = "/$fileName"

    val inputStream = object {}.javaClass.getResourceAsStream(filePath)
        ?: throw IllegalArgumentException("File not found in JAR: $filePath")

    val tempFile = File(tempDir, filePath)
    Files.copy(inputStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
    tempFile.deleteOnExit()

    return tempFile
}
