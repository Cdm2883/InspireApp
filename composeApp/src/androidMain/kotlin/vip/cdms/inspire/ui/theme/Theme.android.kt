package vip.cdms.inspire.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

var AppTheme.dynamicColor by AppTheme.keyOf("dynamicColor") { true }

@Composable
actual fun AppTheme(
    darkTheme: Boolean,
    contrast: Contrast,
    content: @Composable () -> Unit
) {
    val (defaultColorScheme, extendedColorScheme) = getDefaultColorSchemes(darkTheme, contrast)
    val colorScheme = when {
        AppTheme.dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        else -> defaultColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) SideEffect {
        val window = (view.context as Activity).window
        window.statusBarColor = colorScheme.primary.toArgb()
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
    }

    AppTheme0(
        colorScheme,
        extendedColorScheme,
        content
    )
}
