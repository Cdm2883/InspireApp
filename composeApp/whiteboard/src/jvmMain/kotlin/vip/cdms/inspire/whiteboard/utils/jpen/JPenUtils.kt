package vip.cdms.inspire.whiteboard.utils.jpen

import jpen.PenProvider
import jpen.provider.osx.CocoaProvider
import jpen.provider.wintab.WintabProvider
import jpen.provider.xinput.XinputProvider
import vip.cdms.inspire.utils.JvmUtils
import vip.cdms.inspire.whiteboard.utils.copyToJavaLibraryPath
import vip.cdms.inspire.whiteboard.utils.tempExtractInsideJar

object JPenUtils {
    @JvmStatic
    private var loadedNativeLibrary = false
    @JvmStatic
    fun loadNativeLibrary() = loadedNativeLibrary || listOf(
        "libjpen-2-4.so",
        "libjpen-2-4-x86_64.so",
        "jpen-2-3.dll",
        "jpen-2-3-64.dll",
        "libjpen-2-3.jnilib",
    ).any { name ->  // build system will only package the native library of the current system
        runCatching {
            val lib = JvmUtils.tempExtractInsideJar("jpen/$name")
            // fallback if packaged all the libraries
            JvmUtils.copyToJavaLibraryPath(lib) && runCatching { System.loadLibrary(name.substringBeforeLast(".")) }.isSuccess
        }.getOrDefault(false)
    }.also { loadedNativeLibrary = it }

    @JvmStatic
    fun getPenOnlyProviderConstructors() = listOf<PenProvider.Constructor>(
//        jpen.provider.system.SystemProvider.Constructor(),  // only mouse & keyboard
        XinputProvider.Constructor(),
        WintabProvider.Constructor(),
        CocoaProvider.Constructor())
}
