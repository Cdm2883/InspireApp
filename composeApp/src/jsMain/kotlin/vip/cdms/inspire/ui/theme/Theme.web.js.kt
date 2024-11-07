package vip.cdms.inspire.ui.theme

import androidx.compose.ui.graphics.Color
import kotlinx.browser.document

actual fun setBrowserThemeColorMeta(color: Color) {
    (document.getElementById(MetaThemeColorID) ?: document.createElement("meta").apply {
        setAttribute("id", MetaThemeColorID)
        setAttribute("name", "theme-color")
        document.head?.appendChild(this)
    }).setAttribute("content", color.toHex())
}
