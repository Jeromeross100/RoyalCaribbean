package com.rc.app.ui.theme

// app/src/main/java/com/rc/app/ui/theme/RCTheme.kt

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val OceanBlue = Color(0xFF0B2A5A)     // deep ocean
private val RoyalBlue = Color(0xFF1247A6)     // brand-ish primary
private val Sky = Color(0xFF4DA3FF)           // accent
private val Sand = Color(0xFFF5D27A)          // gold accent
private val Mist = Color(0xFFF4F2F8)          // surfaces
private val OnMist = Color(0xFF1A1A1A)

private val LightColors = lightColorScheme(
    primary = RoyalBlue,
    onPrimary = Color.White,
    primaryContainer = OceanBlue,
    onPrimaryContainer = Color.White,
    secondary = Sand,
    onSecondary = Color(0xFF3B2F00),
    background = Mist,
    onBackground = OnMist,
    surface = Color.White,
    onSurface = OnMist,
    outline = Color(0xFFE2E0EA)
)

private val DarkColors = darkColorScheme(
    primary = Sky,
    onPrimary = Color(0xFF001330),
    primaryContainer = OceanBlue,
    onPrimaryContainer = Color.White,
    secondary = Sand,
    onSecondary = Color(0xFF2A2300),
    background = Color(0xFF0C1020),
    onBackground = Color(0xFFE8E8EE),
    surface = Color(0xFF12162A),
    onSurface = Color(0xFFE8E8EE),
    outline = Color(0xFF2B3352)
)

private val RcTypography = Typography(
    displaySmall = TextStyle(fontSize = 34.sp, fontWeight = FontWeight.Bold),
    headlineSmall = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.SemiBold),
    titleLarge   = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold),
    titleMedium  = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold),
    bodyLarge    = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Normal),
    bodyMedium   = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal),
    labelLarge   = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
)

@Composable
fun RCTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors: ColorScheme = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        typography = RcTypography,
        content = content
    )
}

