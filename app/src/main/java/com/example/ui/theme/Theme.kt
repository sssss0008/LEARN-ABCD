package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val LightColorScheme = lightColorScheme(
    primary = KidsIndigoPrimary,
    onPrimary = Color.White,
    primaryContainer = KidsIndigoLight.copy(alpha = 0.2f),
    onPrimaryContainer = KidsIndigoDark,
    secondary = KidsStrawberry,
    onSecondary = Color.White,
    secondaryContainer = KidsStrawberry.copy(alpha = 0.15f),
    onSecondaryContainer = KidsStrawberry,
    tertiary = KidsSunYellow,
    onTertiary = KidsOnBackground,
    tertiaryContainer = KidsSunYellow.copy(alpha = 0.2f),
    onTertiaryContainer = KidsOrange,
    background = KidsBackgroundLight,
    onBackground = KidsOnBackground,
    surface = KidsSurfaceLight,
    onSurface = KidsOnSurface,
    surfaceVariant = KidsSurfaceVariant,
    onSurfaceVariant = KidsTextMuted
)

private val DarkColorScheme = darkColorScheme(
    primary = KidsIndigoLight,
    onPrimary = KidsIndigoDark,
    secondary = KidsStrawberry,
    onSecondary = Color.White,
    tertiary = KidsSunYellow,
    onTertiary = KidsOnBackground,
    background = KidsBackgroundDark,
    onBackground = Color.White,
    surface = KidsSurfaceDark,
    onSurface = Color.White
)

val KidsShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(36.dp)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = KidsShapes,
        content = content
    )
}
