package vip.cdms.inspire.fluent

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import vip.cdms.inspire.fluent.ui.theme.AppTheme

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Inspire",
    ) {
        AppTheme {
            App()
        }
    }
}
