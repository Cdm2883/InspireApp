-keep class com.sun.jna.** { *; }
-keep class * implements com.sun.jna.** { *; }

-dontwarn kotlinx.**
-keep class kotlinx.** { *; }
-dontwarn androidx.compose.**
-keep class androidx.compose.** { *; }
-keep class kotlin.** { *; }
