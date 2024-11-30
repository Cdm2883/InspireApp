package vip.cdms.inspire.utils

import java.io.File

object JvmUtils {
    val CodeLocation: File
        inline get() = File(object {}.javaClass.protectionDomain?.codeSource?.location?.path!!).parentFile
}
