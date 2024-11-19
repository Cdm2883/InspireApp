package vip.cdms.inspire.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

val GlobalContext: Context? by lazy { getGlobalApplication() }

@SuppressLint("PrivateApi")
fun getGlobalApplication() = try {
    (Class.forName("android.app.ActivityThread")
        .getDeclaredMethod("currentApplication")  // https://cs.android.com/android-studio/platform/tools/base/+/mirror-goog-studio-main:fakeandroid/srcs/android/app/ActivityThread.java;l=44
        .apply { isAccessible = true }(null)
        ?: Class.forName("android.app.ActivityThread")
            .getDeclaredMethod("getInitialApplication")  // https://cs.android.com/android/platform/superproject/main/+/main:frameworks/base/core/java/android/app/AppGlobals.java;l=33
            .apply { isAccessible = true }(null)
    ) as Application?
} catch (_: Exception) { null }
