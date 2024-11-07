package vip.cdms.inspire.ui.theme

import androidx.compose.runtime.Composable

@Composable
actual fun AppTheme(
    darkTheme: Boolean,
    contrast: Contrast,
    content: @Composable () -> Unit
) {
    val (colorScheme, extendedColorScheme) = getDefaultColorSchemes(darkTheme, contrast)
    AppTheme0(
        colorScheme,
        extendedColorScheme,
        content
    )
}
