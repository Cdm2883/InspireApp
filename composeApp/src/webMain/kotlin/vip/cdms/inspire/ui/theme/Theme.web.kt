package vip.cdms.inspire.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color

@Composable
actual fun AppTheme(
    darkTheme: Boolean,
    contrast: Contrast,
    content: @Composable () -> Unit
) {
    val (colorScheme, extendedColorScheme) = getDefaultColorSchemes(darkTheme, contrast)

    remember(colorScheme) { setBrowserThemeColorMeta(colorScheme.primary) }

    AppTheme0(
        colorScheme,
        extendedColorScheme,
        content
    )
}

internal const val MetaThemeColorID = "meta-theme-color"
expect fun setBrowserThemeColorMeta(color: Color)  // TODO: different target but same codes (kotlinx.browser)

fun Color.toHex(): String {
    fun calc(component: Float) = (component * 255).toInt()
        .toString(16).padStart(2, '0')
    val red = calc(red)
    val green = calc(green)
    val blue = calc(blue)
    return "#$red$green$blue"
}
