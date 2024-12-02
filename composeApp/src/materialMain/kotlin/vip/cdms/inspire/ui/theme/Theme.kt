@file:Suppress("DuplicatedCode")

package vip.cdms.inspire.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import vip.cdms.inspire.storage.data.KVStorage
import vip.cdms.inspire.storage.data.StorageProvider
import vip.cdms.inspire.storage.data.StorageRoot

@Immutable
data class ExtendedColorScheme(
    val red: ColorFamily,
    val orange: ColorFamily,
    val yellow: ColorFamily,
    val green: ColorFamily,
    val cyan: ColorFamily,
    val blue: ColorFamily,
    val violet: ColorFamily,
)

private val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)

private val mediumContrastLightColorScheme = lightColorScheme(
    primary = primaryLightMediumContrast,
    onPrimary = onPrimaryLightMediumContrast,
    primaryContainer = primaryContainerLightMediumContrast,
    onPrimaryContainer = onPrimaryContainerLightMediumContrast,
    secondary = secondaryLightMediumContrast,
    onSecondary = onSecondaryLightMediumContrast,
    secondaryContainer = secondaryContainerLightMediumContrast,
    onSecondaryContainer = onSecondaryContainerLightMediumContrast,
    tertiary = tertiaryLightMediumContrast,
    onTertiary = onTertiaryLightMediumContrast,
    tertiaryContainer = tertiaryContainerLightMediumContrast,
    onTertiaryContainer = onTertiaryContainerLightMediumContrast,
    error = errorLightMediumContrast,
    onError = onErrorLightMediumContrast,
    errorContainer = errorContainerLightMediumContrast,
    onErrorContainer = onErrorContainerLightMediumContrast,
    background = backgroundLightMediumContrast,
    onBackground = onBackgroundLightMediumContrast,
    surface = surfaceLightMediumContrast,
    onSurface = onSurfaceLightMediumContrast,
    surfaceVariant = surfaceVariantLightMediumContrast,
    onSurfaceVariant = onSurfaceVariantLightMediumContrast,
    outline = outlineLightMediumContrast,
    outlineVariant = outlineVariantLightMediumContrast,
    scrim = scrimLightMediumContrast,
    inverseSurface = inverseSurfaceLightMediumContrast,
    inverseOnSurface = inverseOnSurfaceLightMediumContrast,
    inversePrimary = inversePrimaryLightMediumContrast,
    surfaceDim = surfaceDimLightMediumContrast,
    surfaceBright = surfaceBrightLightMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestLightMediumContrast,
    surfaceContainerLow = surfaceContainerLowLightMediumContrast,
    surfaceContainer = surfaceContainerLightMediumContrast,
    surfaceContainerHigh = surfaceContainerHighLightMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestLightMediumContrast,
)

private val highContrastLightColorScheme = lightColorScheme(
    primary = primaryLightHighContrast,
    onPrimary = onPrimaryLightHighContrast,
    primaryContainer = primaryContainerLightHighContrast,
    onPrimaryContainer = onPrimaryContainerLightHighContrast,
    secondary = secondaryLightHighContrast,
    onSecondary = onSecondaryLightHighContrast,
    secondaryContainer = secondaryContainerLightHighContrast,
    onSecondaryContainer = onSecondaryContainerLightHighContrast,
    tertiary = tertiaryLightHighContrast,
    onTertiary = onTertiaryLightHighContrast,
    tertiaryContainer = tertiaryContainerLightHighContrast,
    onTertiaryContainer = onTertiaryContainerLightHighContrast,
    error = errorLightHighContrast,
    onError = onErrorLightHighContrast,
    errorContainer = errorContainerLightHighContrast,
    onErrorContainer = onErrorContainerLightHighContrast,
    background = backgroundLightHighContrast,
    onBackground = onBackgroundLightHighContrast,
    surface = surfaceLightHighContrast,
    onSurface = onSurfaceLightHighContrast,
    surfaceVariant = surfaceVariantLightHighContrast,
    onSurfaceVariant = onSurfaceVariantLightHighContrast,
    outline = outlineLightHighContrast,
    outlineVariant = outlineVariantLightHighContrast,
    scrim = scrimLightHighContrast,
    inverseSurface = inverseSurfaceLightHighContrast,
    inverseOnSurface = inverseOnSurfaceLightHighContrast,
    inversePrimary = inversePrimaryLightHighContrast,
    surfaceDim = surfaceDimLightHighContrast,
    surfaceBright = surfaceBrightLightHighContrast,
    surfaceContainerLowest = surfaceContainerLowestLightHighContrast,
    surfaceContainerLow = surfaceContainerLowLightHighContrast,
    surfaceContainer = surfaceContainerLightHighContrast,
    surfaceContainerHigh = surfaceContainerHighLightHighContrast,
    surfaceContainerHighest = surfaceContainerHighestLightHighContrast,
)

private val mediumContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkMediumContrast,
    onPrimary = onPrimaryDarkMediumContrast,
    primaryContainer = primaryContainerDarkMediumContrast,
    onPrimaryContainer = onPrimaryContainerDarkMediumContrast,
    secondary = secondaryDarkMediumContrast,
    onSecondary = onSecondaryDarkMediumContrast,
    secondaryContainer = secondaryContainerDarkMediumContrast,
    onSecondaryContainer = onSecondaryContainerDarkMediumContrast,
    tertiary = tertiaryDarkMediumContrast,
    onTertiary = onTertiaryDarkMediumContrast,
    tertiaryContainer = tertiaryContainerDarkMediumContrast,
    onTertiaryContainer = onTertiaryContainerDarkMediumContrast,
    error = errorDarkMediumContrast,
    onError = onErrorDarkMediumContrast,
    errorContainer = errorContainerDarkMediumContrast,
    onErrorContainer = onErrorContainerDarkMediumContrast,
    background = backgroundDarkMediumContrast,
    onBackground = onBackgroundDarkMediumContrast,
    surface = surfaceDarkMediumContrast,
    onSurface = onSurfaceDarkMediumContrast,
    surfaceVariant = surfaceVariantDarkMediumContrast,
    onSurfaceVariant = onSurfaceVariantDarkMediumContrast,
    outline = outlineDarkMediumContrast,
    outlineVariant = outlineVariantDarkMediumContrast,
    scrim = scrimDarkMediumContrast,
    inverseSurface = inverseSurfaceDarkMediumContrast,
    inverseOnSurface = inverseOnSurfaceDarkMediumContrast,
    inversePrimary = inversePrimaryDarkMediumContrast,
    surfaceDim = surfaceDimDarkMediumContrast,
    surfaceBright = surfaceBrightDarkMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkMediumContrast,
    surfaceContainerLow = surfaceContainerLowDarkMediumContrast,
    surfaceContainer = surfaceContainerDarkMediumContrast,
    surfaceContainerHigh = surfaceContainerHighDarkMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkMediumContrast,
)

private val highContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkHighContrast,
    onPrimary = onPrimaryDarkHighContrast,
    primaryContainer = primaryContainerDarkHighContrast,
    onPrimaryContainer = onPrimaryContainerDarkHighContrast,
    secondary = secondaryDarkHighContrast,
    onSecondary = onSecondaryDarkHighContrast,
    secondaryContainer = secondaryContainerDarkHighContrast,
    onSecondaryContainer = onSecondaryContainerDarkHighContrast,
    tertiary = tertiaryDarkHighContrast,
    onTertiary = onTertiaryDarkHighContrast,
    tertiaryContainer = tertiaryContainerDarkHighContrast,
    onTertiaryContainer = onTertiaryContainerDarkHighContrast,
    error = errorDarkHighContrast,
    onError = onErrorDarkHighContrast,
    errorContainer = errorContainerDarkHighContrast,
    onErrorContainer = onErrorContainerDarkHighContrast,
    background = backgroundDarkHighContrast,
    onBackground = onBackgroundDarkHighContrast,
    surface = surfaceDarkHighContrast,
    onSurface = onSurfaceDarkHighContrast,
    surfaceVariant = surfaceVariantDarkHighContrast,
    onSurfaceVariant = onSurfaceVariantDarkHighContrast,
    outline = outlineDarkHighContrast,
    outlineVariant = outlineVariantDarkHighContrast,
    scrim = scrimDarkHighContrast,
    inverseSurface = inverseSurfaceDarkHighContrast,
    inverseOnSurface = inverseOnSurfaceDarkHighContrast,
    inversePrimary = inversePrimaryDarkHighContrast,
    surfaceDim = surfaceDimDarkHighContrast,
    surfaceBright = surfaceBrightDarkHighContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkHighContrast,
    surfaceContainerLow = surfaceContainerLowDarkHighContrast,
    surfaceContainer = surfaceContainerDarkHighContrast,
    surfaceContainerHigh = surfaceContainerHighDarkHighContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkHighContrast,
)

val extendedLight = ExtendedColorScheme(
    red = ColorFamily(
        redLight,
        onRedLight,
        redContainerLight,
        onRedContainerLight,
    ),
    orange = ColorFamily(
        orangeLight,
        onOrangeLight,
        orangeContainerLight,
        onOrangeContainerLight,
    ),
    yellow = ColorFamily(
        yellowLight,
        onYellowLight,
        yellowContainerLight,
        onYellowContainerLight,
    ),
    green = ColorFamily(
        greenLight,
        onGreenLight,
        greenContainerLight,
        onGreenContainerLight,
    ),
    cyan = ColorFamily(
        cyanLight,
        onCyanLight,
        cyanContainerLight,
        onCyanContainerLight,
    ),
    blue = ColorFamily(
        blueLight,
        onBlueLight,
        blueContainerLight,
        onBlueContainerLight,
    ),
    violet = ColorFamily(
        violetLight,
        onVioletLight,
        violetContainerLight,
        onVioletContainerLight,
    ),
)

val extendedDark = ExtendedColorScheme(
    red = ColorFamily(
        redDark,
        onRedDark,
        redContainerDark,
        onRedContainerDark,
    ),
    orange = ColorFamily(
        orangeDark,
        onOrangeDark,
        orangeContainerDark,
        onOrangeContainerDark,
    ),
    yellow = ColorFamily(
        yellowDark,
        onYellowDark,
        yellowContainerDark,
        onYellowContainerDark,
    ),
    green = ColorFamily(
        greenDark,
        onGreenDark,
        greenContainerDark,
        onGreenContainerDark,
    ),
    cyan = ColorFamily(
        cyanDark,
        onCyanDark,
        cyanContainerDark,
        onCyanContainerDark,
    ),
    blue = ColorFamily(
        blueDark,
        onBlueDark,
        blueContainerDark,
        onBlueContainerDark,
    ),
    violet = ColorFamily(
        violetDark,
        onVioletDark,
        violetContainerDark,
        onVioletContainerDark,
    ),
)

val extendedLightMediumContrast = ExtendedColorScheme(
    red = ColorFamily(
        redLightMediumContrast,
        onRedLightMediumContrast,
        redContainerLightMediumContrast,
        onRedContainerLightMediumContrast,
    ),
    orange = ColorFamily(
        orangeLightMediumContrast,
        onOrangeLightMediumContrast,
        orangeContainerLightMediumContrast,
        onOrangeContainerLightMediumContrast,
    ),
    yellow = ColorFamily(
        yellowLightMediumContrast,
        onYellowLightMediumContrast,
        yellowContainerLightMediumContrast,
        onYellowContainerLightMediumContrast,
    ),
    green = ColorFamily(
        greenLightMediumContrast,
        onGreenLightMediumContrast,
        greenContainerLightMediumContrast,
        onGreenContainerLightMediumContrast,
    ),
    cyan = ColorFamily(
        cyanLightMediumContrast,
        onCyanLightMediumContrast,
        cyanContainerLightMediumContrast,
        onCyanContainerLightMediumContrast,
    ),
    blue = ColorFamily(
        blueLightMediumContrast,
        onBlueLightMediumContrast,
        blueContainerLightMediumContrast,
        onBlueContainerLightMediumContrast,
    ),
    violet = ColorFamily(
        violetLightMediumContrast,
        onVioletLightMediumContrast,
        violetContainerLightMediumContrast,
        onVioletContainerLightMediumContrast,
    ),
)

val extendedLightHighContrast = ExtendedColorScheme(
    red = ColorFamily(
        redLightHighContrast,
        onRedLightHighContrast,
        redContainerLightHighContrast,
        onRedContainerLightHighContrast,
    ),
    orange = ColorFamily(
        orangeLightHighContrast,
        onOrangeLightHighContrast,
        orangeContainerLightHighContrast,
        onOrangeContainerLightHighContrast,
    ),
    yellow = ColorFamily(
        yellowLightHighContrast,
        onYellowLightHighContrast,
        yellowContainerLightHighContrast,
        onYellowContainerLightHighContrast,
    ),
    green = ColorFamily(
        greenLightHighContrast,
        onGreenLightHighContrast,
        greenContainerLightHighContrast,
        onGreenContainerLightHighContrast,
    ),
    cyan = ColorFamily(
        cyanLightHighContrast,
        onCyanLightHighContrast,
        cyanContainerLightHighContrast,
        onCyanContainerLightHighContrast,
    ),
    blue = ColorFamily(
        blueLightHighContrast,
        onBlueLightHighContrast,
        blueContainerLightHighContrast,
        onBlueContainerLightHighContrast,
    ),
    violet = ColorFamily(
        violetLightHighContrast,
        onVioletLightHighContrast,
        violetContainerLightHighContrast,
        onVioletContainerLightHighContrast,
    ),
)

val extendedDarkMediumContrast = ExtendedColorScheme(
    red = ColorFamily(
        redDarkMediumContrast,
        onRedDarkMediumContrast,
        redContainerDarkMediumContrast,
        onRedContainerDarkMediumContrast,
    ),
    orange = ColorFamily(
        orangeDarkMediumContrast,
        onOrangeDarkMediumContrast,
        orangeContainerDarkMediumContrast,
        onOrangeContainerDarkMediumContrast,
    ),
    yellow = ColorFamily(
        yellowDarkMediumContrast,
        onYellowDarkMediumContrast,
        yellowContainerDarkMediumContrast,
        onYellowContainerDarkMediumContrast,
    ),
    green = ColorFamily(
        greenDarkMediumContrast,
        onGreenDarkMediumContrast,
        greenContainerDarkMediumContrast,
        onGreenContainerDarkMediumContrast,
    ),
    cyan = ColorFamily(
        cyanDarkMediumContrast,
        onCyanDarkMediumContrast,
        cyanContainerDarkMediumContrast,
        onCyanContainerDarkMediumContrast,
    ),
    blue = ColorFamily(
        blueDarkMediumContrast,
        onBlueDarkMediumContrast,
        blueContainerDarkMediumContrast,
        onBlueContainerDarkMediumContrast,
    ),
    violet = ColorFamily(
        violetDarkMediumContrast,
        onVioletDarkMediumContrast,
        violetContainerDarkMediumContrast,
        onVioletContainerDarkMediumContrast,
    ),
)

val extendedDarkHighContrast = ExtendedColorScheme(
    red = ColorFamily(
        redDarkHighContrast,
        onRedDarkHighContrast,
        redContainerDarkHighContrast,
        onRedContainerDarkHighContrast,
    ),
    orange = ColorFamily(
        orangeDarkHighContrast,
        onOrangeDarkHighContrast,
        orangeContainerDarkHighContrast,
        onOrangeContainerDarkHighContrast,
    ),
    yellow = ColorFamily(
        yellowDarkHighContrast,
        onYellowDarkHighContrast,
        yellowContainerDarkHighContrast,
        onYellowContainerDarkHighContrast,
    ),
    green = ColorFamily(
        greenDarkHighContrast,
        onGreenDarkHighContrast,
        greenContainerDarkHighContrast,
        onGreenContainerDarkHighContrast,
    ),
    cyan = ColorFamily(
        cyanDarkHighContrast,
        onCyanDarkHighContrast,
        cyanContainerDarkHighContrast,
        onCyanContainerDarkHighContrast,
    ),
    blue = ColorFamily(
        blueDarkHighContrast,
        onBlueDarkHighContrast,
        blueContainerDarkHighContrast,
        onBlueContainerDarkHighContrast,
    ),
    violet = ColorFamily(
        violetDarkHighContrast,
        onVioletDarkHighContrast,
        violetContainerDarkHighContrast,
        onVioletContainerDarkHighContrast,
    ),
)

@Immutable
data class ColorFamily(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color
)

//val unspecified_scheme = ColorFamily(
//    Color.Unspecified, Color.Unspecified, Color.Unspecified, Color.Unspecified
//)

internal val LocalExtendedColorScheme = staticCompositionLocalOf { extendedLight }
@Suppress("UnusedReceiverParameter")
val MaterialTheme.extendedColorScheme
    @Composable @ReadOnlyComposable get() = LocalExtendedColorScheme.current

enum class Contrast { Normal, Medium, High }

fun getDefaultColorSchemes(darkTheme: Boolean, contrast: Contrast) = when {
    darkTheme -> when (contrast) {
        Contrast.Normal -> darkScheme to extendedDark
        Contrast.Medium -> mediumContrastDarkColorScheme to extendedDarkMediumContrast
        Contrast.High -> highContrastDarkColorScheme to extendedDarkHighContrast
    }
    else -> when (contrast) {
        Contrast.Normal -> lightScheme to extendedLight
        Contrast.Medium -> mediumContrastLightColorScheme to extendedLightMediumContrast
        Contrast.High -> highContrastLightColorScheme to extendedLightHighContrast
    }
}

@Composable
internal fun AppTheme0(
    colorScheme: ColorScheme,
    extendedColorScheme: ExtendedColorScheme,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalExtendedColorScheme provides extendedColorScheme
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            content = content
        )
    }
}

object AppTheme : KVStorage.KeyOf("theme"), StorageProvider by StorageRoot {
    var darkTheme by keyOf<Boolean>("dark")
    val darkThemeOrSystem
        @Composable
        get() = darkTheme ?: isSystemInDarkTheme()
    var contrast by keyOf("contrast") { Contrast.Normal }
}

@Composable
expect fun AppTheme(
    darkTheme: Boolean = AppTheme.darkThemeOrSystem,
    contrast: Contrast = AppTheme.contrast,
    content: @Composable () -> Unit
)