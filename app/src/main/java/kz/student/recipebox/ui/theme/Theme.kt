package kz.student.recipebox.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// The only place with spacing values. Screens use Spacing.md, never 16.dp directly.
object Spacing {
 val xs = 4.dp
 val sm = 8.dp
 val md = 16.dp
 val lg = 24.dp
 val touch = 48.dp
}

// The only place with color values. Screens use MaterialTheme.colorScheme.
// Warm "kitchen" palette: terracotta primary, olive secondary.
private val LightColors = lightColorScheme(
 primary = Color(0xFF9A4A24), onPrimary = Color(0xFFFFFFFF),
 primaryContainer = Color(0xFFFFDBCB), onPrimaryContainer = Color(0xFF3A1606),
 secondary = Color(0xFF5E6B3A), onSecondary = Color(0xFFFFFFFF),
 secondaryContainer = Color(0xFFE2EDB9), onSecondaryContainer = Color(0xFF1B2100),
 background = Color(0xFFFFF8F4), onBackground = Color(0xFF221A16),
 surface = Color(0xFFFFF8F4), onSurface = Color(0xFF221A16),
 surfaceVariant = Color(0xFFF4DED4), onSurfaceVariant = Color(0xFF52443D),
 surfaceContainer = Color(0xFFFCEAE2),
 outline = Color(0xFF85736B)
)
private val DarkColors = darkColorScheme(
 primary = Color(0xFFFFB693), onPrimary = Color(0xFF5A1F04),
 primaryContainer = Color(0xFF7A3410), onPrimaryContainer = Color(0xFFFFDBCB),
 secondary = Color(0xFFC6D19F), onSecondary = Color(0xFF2F3410),
 secondaryContainer = Color(0xFF464F24), onSecondaryContainer = Color(0xFFE2EDB9),
 background = Color(0xFF1A120E), onBackground = Color(0xFFF0DFD8),
 surface = Color(0xFF1A120E), onSurface = Color(0xFFF0DFD8),
 surfaceVariant = Color(0xFF52443D), onSurfaceVariant = Color(0xFFD7C2B9),
 surfaceContainer = Color(0xFF271E1A),
 outline = Color(0xFFA08D85)
)
// The only place with font sizes. Screens use MaterialTheme.typography.
private val AppTypography = Typography(
 headlineLarge = TextStyle(fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 30.sp, lineHeight = 36.sp),
 headlineMedium = TextStyle(fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 26.sp, lineHeight = 32.sp),
 titleLarge = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 22.sp, lineHeight = 28.sp),
 titleMedium = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 17.sp, lineHeight = 24.sp),
 bodyLarge = TextStyle(fontSize = 16.sp, lineHeight = 24.sp),
 bodyMedium = TextStyle(fontSize = 14.sp, lineHeight = 20.sp),
 labelLarge = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 14.sp, lineHeight = 20.sp),
 labelMedium = TextStyle(fontWeight = FontWeight.Medium, fontSize = 12.sp, lineHeight = 16.sp)
)

@Composable
fun RecipeBoxTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
 MaterialTheme(colorScheme = if (darkTheme) DarkColors else LightColors, typography = AppTypography, content = content)
}
