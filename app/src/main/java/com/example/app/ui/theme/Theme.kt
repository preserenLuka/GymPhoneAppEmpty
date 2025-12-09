package com.example.app.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Theme options used across the app
enum class ThemeOption {
    LIGHT,
    DARK,
    NIGHT_BLUE,
    JADE_GREEN
}

// ----- BASE SCHEMES (brand light/dark) -----

private val LightColorScheme = lightColorScheme(
    primary = BluePrimary,
    onPrimary = PureWhite,
    primaryContainer = BlueSecondary,
    onPrimaryContainer = PureWhite,
    secondary = TealAccent,
    onSecondary = PureWhite,
    background = LightGray,
    onBackground = NeutralDark,
    surface = PureWhite,
    onSurface = NeutralDark,
    surfaceVariant = PureWhite,
    outline = OutlineGray,
    error = ErrorRed,
    onError = PureWhite
)

private val DarkColorScheme = darkColorScheme(
    primary = BluePrimary,
    onPrimary = PureWhite,
    primaryContainer = BlueSecondary,
    onPrimaryContainer = PureWhite,
    secondary = TealAccent,
    onSecondary = Color.Black,
    background = DarkerGray,    // nevtralen temen siv
    onBackground = PureWhite,
    surface = NeutralDark,
    onSurface = PureWhite,
    surfaceVariant = NeutralDark,
    outline = OutlineGray,
    error = ErrorRed,
    onError = PureWhite
)

// ----- APP THEME WRAPPER -----

@Composable
fun AppTheme(
    themeOption: ThemeOption = ThemeOption.LIGHT,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // ali je tema temna (za dynamicColor / status bar)
    val isDark = when (themeOption) {
        ThemeOption.LIGHT -> false
        ThemeOption.DARK,
        ThemeOption.NIGHT_BLUE,
        ThemeOption.JADE_GREEN -> true
    }

    // izberi osnovno shemo (light/dark ali dynamic)
    val baseScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (isDark) androidx.compose.material3.dynamicDarkColorScheme(context)
            else androidx.compose.material3.dynamicLightColorScheme(context)
        }

        isDark -> DarkColorScheme
        else -> LightColorScheme
    }

    // per-theme prilagoditve
    val colorScheme = when (themeOption) {

        ThemeOption.LIGHT -> baseScheme

        ThemeOption.DARK -> baseScheme // klasika: temno sivo ozadje, tvoja obstoječa dark shema

        ThemeOption.NIGHT_BLUE -> baseScheme.copy(
            // zelo temno modro ozadje, kot na screenshotu
            background = Color(0xFF0A1224),
            onBackground = PureWhite,
            surface = Color(0xFF0B1525),
            onSurface = PureWhite,
            surfaceVariant = Color(0xFF101B30),
            primary = Color(0xFF5A8DFF),      // malo mehkejši modri ton
            onPrimary = PureWhite,
            primaryContainer = Color(0xFF304A80),
            onPrimaryContainer = PureWhite,
            secondary = Color(0xFF3AC0FF),    // hladno modro-cian
            outline = Color(0xFF2E3A55)
        )

        ThemeOption.JADE_GREEN -> baseScheme.copy(
            // temno zeleno ozadje, jade vibe
            background = Color(0xFF06231B),
            onBackground = PureWhite,
            surface = Color(0xFF073026),
            onSurface = PureWhite,
            surfaceVariant = Color(0xFF0B3D30),
            primary = Color(0xFF21C18C),      // jade/emerald zelen
            onPrimary = PureWhite,
            primaryContainer = Color(0xFF0F7A57),
            onPrimaryContainer = PureWhite,
            secondary = YellowAccent,        // malo kontrasta
            outline = Color(0xFF275347)
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
