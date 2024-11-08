package vip.cdms.inspire.fluent.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.WindowScope
import com.konyaco.fluent.ExperimentalFluentApi
import com.konyaco.fluent.FluentTheme
import com.konyaco.fluent.LocalContentColor
import com.mayakapps.compose.windowstyler.WindowBackdrop
import com.mayakapps.compose.windowstyler.WindowStyle

@OptIn(ExperimentalFluentApi::class)
@Composable
fun WindowScope.AppTheme(
    // TODO: get values from settings
    darkTheme: Boolean = isSystemInDarkTheme(),
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
