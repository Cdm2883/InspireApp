package vip.cdms.inspire.service.whiteboard

import androidx.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import jpen.*
import jpen.demo.StatusReport
import jpen.event.PenAdapter
import jpen.event.PenListener
import jpen.owner.AbstractPenOwner
import jpen.owner.PenClip
import java.awt.Point
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import kotlin.concurrent.thread

private class RegularPenClip : PenClip {
    override fun evalLocationOnScreen(pointOnScreen: Point) {
    }

    override operator fun contains(point: java.awt.geom.Point2D.Float): Boolean {
        return true
    }
}

private class RegularPenOwner : AbstractPenOwner() {
    override fun getPenProviderConstructors(): Collection<PenProvider.Constructor> {
        return java.util.Arrays.asList<PenProvider.Constructor>(
            //jpen.provider.system.SystemProvider.Constructor(),
            jpen.provider.xinput.XinputProvider.Constructor(),
            jpen.provider.wintab.WintabProvider.Constructor(),
            jpen.provider.osx.CocoaProvider.Constructor())
    }

    private val penClip: RegularPenClip = RegularPenClip()

    override fun getPenClip(): PenClip {
        return penClip
    }

    override fun draggingOutDisengaged() {
    }

    override fun init() {
        penManagerHandle.setPenManagerPaused(false)
    }
}

private class JPenExample {
    private val penOwner: RegularPenOwner = RegularPenOwner()
    private val penManager: PenManager = PenManager(penOwner)

    constructor(penListener: PenListener) {
        println(StatusReport(penManager))
        penManager.pen.addListener(penListener)
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Inspire Whiteboard",
    ) {
        val messages = remember { mutableStateListOf<String>() }

        SideEffect {
            (messages.size - 36).let { if (it > 0) messages.removeRange(0, it) }
        }
        Text(messages.joinToString("\n"))

        LaunchedEffect(Unit) {

            listOf(
                "libjpen-2-4.so",
                "libjpen-2-4-x86_64.so",
                "jpen-2-3.dll",
                "jpen-2-3-64.dll",
                "libjpen-2-3.jnilib",
            ).forEach { lib ->
                runCatching {
                    val file = tempExtractInsideJar("jpen/$lib")
                    copyToJavaLibraryPath(file)
                }
            }

            thread {
                JPenExample((object : PenAdapter() {
                    private var pen: Pen? = null

                    override fun penLevelEvent(ev: PLevelEvent?) {
                        if (null == pen) pen = (ev ?: return).pen
                    }

                    override fun penTock(availableMillis: Long) {
                        messages += ("penTock availableMillis: $availableMillis")
                        val _pen = pen ?: return

                        messages += ("KIND ${_pen.kind}")

                        for (buttonType in PButton.Type.VALUES) {
                            if (_pen.getButtonValue(buttonType)) {
                                messages += ("BUTTON_TYPE $buttonType")
                            }
                        }

                        for (levelType in PLevel.Type.VALUES){
                            val value = _pen.getLevelValue(levelType)
                            messages += ("LEVEL type: ${levelType}, value: $value")
                        }

                        // only process tocks after an event happens
                        pen = null
                    }
                }))
            }

        }
    }
}

fun copyToJavaLibraryPath(libraryFile: File) = System.getProperty("java.library.path").split(File.pathSeparator).any { libraryPath ->
    val targetDir = File(libraryPath).takeIf { it.exists() && it.isDirectory } ?: return@any false
    val targetFile = File(targetDir, libraryFile.name)
    Files.copy(libraryFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
    targetFile.deleteOnExit()
    false
}

fun tempDir(prefix: String) = Files.createTempDirectory(prefix).toFile().apply { deleteOnExit() }
fun tempExtractInsideJar(
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
