package com.example.app.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = BluePrimary,
    onPrimary = Color.White,
    primaryContainer = BlueSecondary,
    onPrimaryContainer = Color.White,

    secondary = TealAccent,
    onSecondary = Color.White,

    background = DarkerGray,
    onBackground = Color.White,

    surface = NeutralDark,
    onSurface = Color.White,

    surfaceVariant = NeutralDark,
    onSurfaceVariant = Color.White,

    outline = OutlineGray,
    error = ErrorRed,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = BluePrimary,
    onPrimary = Color.White,
    primaryContainer = BlueSecondary,
    onPrimaryContainer = Color.White,

    secondary = TealAccent,
    onSecondary = Color.White,

    background = LightGray,     // the soft gray you use behind cards
    onBackground = NeutralDark,

    surface = PureWhite,        // cards
    onSurface = NeutralDark,

    surfaceVariant = PureWhite,
    onSurfaceVariant = NeutralDark,

    outline = OutlineGray,
    error = ErrorRed,
    onError = Color.White
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // if you want strict brand colors, keep this false
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // if you REALLY want dynamic colors on Android 12+, you can re-enable here
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) androidx.compose.material3.dynamicDarkColorScheme(context)
            else androidx.compose.material3.dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
