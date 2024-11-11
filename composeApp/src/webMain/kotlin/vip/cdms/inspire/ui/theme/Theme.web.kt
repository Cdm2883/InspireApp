package vip.cdms.inspire.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import web.document

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

private const val MetaThemeColorID = "meta-theme-color"
private fun setBrowserThemeColorMeta(color: Color) =
    (document.getElementById(MetaThemeColorID) ?: document.createElement("meta").apply {
        setAttribute("id", MetaThemeColorID)
        setAttribute("name", "theme-color")
        document.head?.appendChild(this)
    }).setAttribute("content", color.toHex())

private fun Color.toHex() = (fun (component: Float) = (component * 255).toInt()
    .toString(16).padStart(2, '0'))
    .let { "#${it(red)}${it(green)}${it(blue)}" }
