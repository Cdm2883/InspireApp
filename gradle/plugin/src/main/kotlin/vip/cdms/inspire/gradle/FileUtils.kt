package vip.cdms.inspire.gradle

import java.io.File

fun File.transformText(transformer: String.() -> String) = writeText(transformer(readText()))
