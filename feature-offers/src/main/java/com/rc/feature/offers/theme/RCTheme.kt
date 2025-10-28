package com.rc.feature.offers.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Shapes
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

// Palette inspired by Royal Caribbean
val RCIndigo = Color(0xFF1A2365)   // deep navy
val RCPurple = Color(0xFF4F2D89)   // royal
val RCLilac = Color(0xFFEDE7F6)    // subtle card bg
val RCGold = Color(0xFFF1C40F)     // accent
val RCSea = Color(0xFF00A4D6)      // sea blue
val RCOnNavy = Color(0xFFEFF2FF)

private val RcLightColors: ColorScheme = lightColorScheme(
    primary = RCPurple,
    onPrimary = Color.White,
    primaryContainer = RCIndigo,
    onPrimaryContainer = RCOnNavy,
    secondary = RCSea,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD6F3FB),
    onSecondaryContainer = RCIndigo,
    background = Color(0xFFFAF8FE),
    onBackground = Color(0xFF1B1B1F),
    surface = Color(0xFFFBF9FF),
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = RCLilac,
    onSurfaceVariant = RCIndigo,
    outline = Color(0xFFB9B5C8),
)

val RcShapes = Shapes(
    extraSmall = RoundedCornerShape(8),
    small = RoundedCornerShape(12),
    medium = RoundedCornerShape(16),
    large = RoundedCornerShape(20),
    extraLarge = RoundedCornerShape(28)
)

// Punchier type for hierarchy
val RcTypography = Typography(
    displaySmall = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold, letterSpacing = (-0.25).sp),
    headlineMedium = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.SemiBold),
    titleLarge = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold),
    titleMedium = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Medium),
    bodyLarge = TextStyle(fontSize = 16.sp),
    bodyMedium = TextStyle(fontSize = 14.sp),
    labelLarge = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold),
)

@Composable
fun RcTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = RcLightColors,
        typography = RcTypography,
        shapes = RcShapes,
        content = content
    )
}
