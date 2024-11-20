package vip.cdms.inspire.utils

import java.io.File

object SelfJar {
    val Location: File by lazy { File(SelfJar.javaClass.protectionDomain?.codeSource?.location?.path!!).parentFile }
}
