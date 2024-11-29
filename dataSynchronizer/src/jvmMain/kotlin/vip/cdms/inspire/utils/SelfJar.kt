package vip.cdms.inspire.utils

import java.io.File

object SelfJar {
    val Location: File
        inline get() = File(object {}.javaClass.protectionDomain?.codeSource?.location?.path!!).parentFile
}
