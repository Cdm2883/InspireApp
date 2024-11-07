package vip.cdms.inspire

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document
import vip.cdms.inspire.ui.theme.AppTheme

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport(document.body!!) {
        AppTheme {
            App()
        }
    }
}
