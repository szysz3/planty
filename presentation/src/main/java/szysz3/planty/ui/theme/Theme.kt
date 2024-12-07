package szysz3.planty.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Light Theme Colors
private val LightColorsScheme = lightColorScheme(
    primary = MutedGreen,
    onPrimary = Color.Black,
    secondary = AccentGreen,
    onSecondary = Color.White,
    background = SoftGrey,
    onBackground = DarkGreen,
    surface = LightGrey,
    onSurface = Color.Black,
    error = ErrorRed,
    onError = Color.White
)

// Dark Theme Colors
private val DarkColorsScheme = darkColorScheme(
    primary = MutedGreen,
    onPrimary = Color.White,
    secondary = AccentTeal,
    onSecondary = Color.White,
    background = DarkGray,
    onBackground = Color(0xFFDADADA),
    surface = DarkBeige,
    onSurface = Color.White,
    error = ErrorRedDark,
    onError = Color.Black,
)

@Composable
fun PlantyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorsScheme
        else -> LightColorsScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
        shapes = Shapes,
    )
}