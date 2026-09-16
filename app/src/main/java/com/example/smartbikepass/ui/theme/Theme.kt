package com.example.smartbikepass.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = DeepNavy,
    onPrimary = SurfaceWhite,
    primaryContainer = NavyLight,
    onPrimaryContainer = SurfaceWhite,
    secondary = SkyBlue,
    onSecondary = SurfaceWhite,
    secondaryContainer = SkyBlueLight,
    onSecondaryContainer = DeepNavy,
    tertiary = EmeraldGreen,
    onTertiary = SurfaceWhite,
    background = BackgroundLight,
    onBackground = TextPrimary,
    surface = SurfaceWhite,
    onSurface = TextPrimary,
    outline = BorderLight,
    error = CrimsonRed,
    onError = SurfaceWhite
)

private val DarkColorScheme = darkColorScheme(
    primary = SkyBlue,
    onPrimary = DeepNavy,
    primaryContainer = NavyLight,
    onPrimaryContainer = SurfaceWhite,
    secondary = SkyBlueLight,
    onSecondary = DeepNavy,
    tertiary = EmeraldGreen,
    background = DeepNavy,
    onBackground = SurfaceWhite,
    surface = NavyLight,
    onSurface = SurfaceWhite,
    outline = TextSecondary,
    error = CrimsonRed
)

@Composable
fun SmartBikePassTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
