package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
  primary = GoldPrimary,
  onPrimary = Graphite950,
  primaryContainer = Graphite800,
  onPrimaryContainer = GoldLight,
  secondary = VioletAccent,
  onSecondary = Graphite950,
  secondaryContainer = Graphite700,
  onSecondaryContainer = VioletSoft,
  tertiary = BlueAccent,
  onTertiary = Graphite950,
  background = Graphite950,
  onBackground = TextHighEmphasis,
  surface = Graphite900,
  onSurface = TextHighEmphasis,
  surfaceVariant = Graphite850,
  onSurfaceVariant = TextMediumEmphasis,
  outline = BorderSubtle,
  outlineVariant = Graphite700
)

private val LightColorScheme = darkColorScheme( // We prefer the signature dark graphite aesthetic for Innovara Notes
  primary = GoldPrimary,
  onPrimary = Graphite950,
  primaryContainer = Graphite800,
  onPrimaryContainer = GoldLight,
  secondary = VioletAccent,
  onSecondary = Graphite950,
  background = Graphite950,
  onBackground = TextHighEmphasis,
  surface = Graphite900,
  onSurface = TextHighEmphasis,
  surfaceVariant = Graphite850,
  onSurfaceVariant = TextMediumEmphasis,
  outline = BorderSubtle
)

@Composable
fun InnovaraTheme(
  darkTheme: Boolean = true,
  content: @Composable () -> Unit
) {
  MaterialTheme(
    colorScheme = DarkColorScheme,
    typography = Typography,
    content = content
  )
}
