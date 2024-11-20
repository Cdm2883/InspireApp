package vip.cdms.inspire.fluent.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.WindowScope
import com.konyaco.fluent.ExperimentalFluentApi
import com.konyaco.fluent.FluentTheme
import com.konyaco.fluent.LocalContentColor
import com.mayakapps.compose.windowstyler.WindowBackdrop
import com.mayakapps.compose.windowstyler.WindowStyle
import vip.cdms.inspire.storage.data.KVStorage
import vip.cdms.inspire.storage.data.StorageProvider
import vip.cdms.inspire.storage.data.StorageRoot

object AppTheme : KVStorage.KeyOf("theme"), StorageProvider by StorageRoot {
    var darkTheme by AppTheme.keyOf<Boolean>("dark")
    val darkThemeOrSystem
        @Composable
        get() = darkTheme ?: isSystemInDarkTheme()
}

@OptIn(ExperimentalFluentApi::class)
@Composable
fun WindowScope.AppTheme(
    darkTheme: Boolean = AppTheme.darkThemeOrSystem,
    content: @Composable () -> Unit
) {
    WindowStyle(
        isDarkTheme = darkTheme,
        backdropType = WindowBackdrop.Mica,
    )

    val colorScheme = if (darkTheme) colorsDark else colorsLight

    FluentTheme(
        colors = colorScheme,
        typography = AppTypography,
        useAcrylicPopup = true,
        compactMode = true
    ) {
        CompositionLocalProvider(
            LocalContentColor provides colorScheme.text.text.primary
        ) {
            content()
        }
    }
}
