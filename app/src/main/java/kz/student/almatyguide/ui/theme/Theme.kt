package kz.student.almatyguide.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object Spacing {
 val xs = 4.dp
 val sm = 8.dp
 val md = 16.dp
 val lg = 24.dp
 val xl = 32.dp
 val touch = 48.dp
}
private val LightColors = lightColorScheme(
 primary = Color(0xFF385C49), onPrimary = Color(0xFFFFFFFF),
 primaryContainer = Color(0xFFDCE9DD), onPrimaryContainer = Color(0xFF173525),
 secondary = Color(0xFF86513D), onSecondary = Color(0xFFFFFFFF),
 secondaryContainer = Color(0xFFF4DDD1), onSecondaryContainer = Color(0xFF4A291B),
 background = Color(0xFFF8F7F2), onBackground = Color(0xFF202A23),
 surface = Color(0xFFF8F7F2), onSurface = Color(0xFF202A23),
 surfaceVariant = Color(0xFFE9EDE4), onSurfaceVariant = Color(0xFF536055),
 outline = Color(0xFF738074)
)
private val DarkColors = darkColorScheme(
 primary = Color(0xFFACD2B7), onPrimary = Color(0xFF173525),
 primaryContainer = Color(0xFF2D4C39), onPrimaryContainer = Color(0xFFDCE9DD),
 secondary = Color(0xFFE9B99E), onSecondary = Color(0xFF482719),
 secondaryContainer = Color(0xFF633C2A), onSecondaryContainer = Color(0xFFF4DDD1),
 background = Color(0xFF121B16), onBackground = Color(0xFFE3EAE1),
 surface = Color(0xFF121B16), onSurface = Color(0xFFE3EAE1),
 surfaceVariant = Color(0xFF2C382F), onSurfaceVariant = Color(0xFFBFCABD),
 outline = Color(0xFF8B9B8E)
)
private val AppTypography = Typography(
 headlineLarge = TextStyle(fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 32.sp, lineHeight = 38.sp),
 headlineMedium = TextStyle(fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 28.sp, lineHeight = 34.sp),
 titleLarge = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 22.sp, lineHeight = 28.sp),
 titleMedium = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 17.sp, lineHeight = 24.sp),
 bodyLarge = TextStyle(fontSize = 16.sp, lineHeight = 25.sp),
 bodyMedium = TextStyle(fontSize = 14.sp, lineHeight = 21.sp),
 labelLarge = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 14.sp, lineHeight = 20.sp)
)
@Composable
fun AlmatyTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
 MaterialTheme(colorScheme = if (darkTheme) DarkColors else LightColors, typography = AppTypography, content = content)
}
