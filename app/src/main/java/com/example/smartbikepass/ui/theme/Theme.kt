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

import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

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
    onPrimary = SurfaceWhite,
    primaryContainer = NavyLight,
    onPrimaryContainer = SurfaceWhite,
    secondary = SkyBlueLight,
    onSecondary = DeepNavy,
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

@Composable
fun appOutlinedTextFieldColors(): TextFieldColors = OutlinedTextFieldDefaults.colors(
    focusedTextColor = TextPrimary,
    unfocusedTextColor = TextPrimary,
    disabledTextColor = TextSecondary,
    errorTextColor = CrimsonRed,
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White,
    disabledContainerColor = Color(0xFFF1F5F9),
    errorContainerColor = Color.White,
    cursorColor = SkyBlue,
    errorCursorColor = CrimsonRed,
    focusedBorderColor = SkyBlue,
    unfocusedBorderColor = BorderLight,
    disabledBorderColor = BorderLight.copy(alpha = 0.5f),
    errorBorderColor = CrimsonRed,
    focusedLabelColor = DeepNavy,
    unfocusedLabelColor = TextSecondary,
    disabledLabelColor = TextSecondary.copy(alpha = 0.5f),
    errorLabelColor = CrimsonRed,
    focusedPlaceholderColor = TextSecondary.copy(alpha = 0.6f),
    unfocusedPlaceholderColor = TextSecondary.copy(alpha = 0.6f),
    disabledPlaceholderColor = TextSecondary.copy(alpha = 0.3f),
    focusedLeadingIconColor = SkyBlue,
    unfocusedLeadingIconColor = TextSecondary,
    disabledLeadingIconColor = TextSecondary.copy(alpha = 0.4f),
    errorLeadingIconColor = CrimsonRed,
    focusedTrailingIconColor = SkyBlue,
    unfocusedTrailingIconColor = TextSecondary,
    disabledTrailingIconColor = TextSecondary.copy(alpha = 0.4f),
    errorTrailingIconColor = CrimsonRed
)

val AppInputTextStyle = TextStyle(
    color = TextPrimary,
    fontSize = 15.sp,
    fontWeight = FontWeight.Normal
)

@Composable
fun SmartBikePassTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = DeepNavy.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
