package szysz3.planty.core.composable

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.toSize
import szysz3.planty.core.composable.EllipticalBackgroundConstants.DEFAULT_TINT_LEVEL_CENTER
import szysz3.planty.core.composable.EllipticalBackgroundConstants.FULL_TINT_LEVEL
import szysz3.planty.core.composable.EllipticalBackgroundConstants.OUTER_TINT_LEVEL
import szysz3.planty.core.composable.EllipticalBackgroundConstants.RADIAL_GRADIENT_RADIUS

private object EllipticalBackgroundConstants {
    const val DEFAULT_TINT_LEVEL_CENTER = 0.3f
    const val OUTER_TINT_LEVEL = 0.7f
    const val FULL_TINT_LEVEL = 1.0f
    const val RADIAL_GRADIENT_RADIUS = 900f
}

/**
 * A composable that displays a background image with a radial gradient overlay.
 * The gradient creates an elliptical vignette effect using the current theme's background color.
 *
 * @param backgroundImageId Resource ID of the background image to display
 * @param tintLevelCenter Alpha value for the center of the radial gradient overlay.
 *                        Lower values make the center more transparent.
 */
@Composable
fun EllipticalBackground(
    @DrawableRes backgroundImageId: Int,
    tintLevelCenter: Float = DEFAULT_TINT_LEVEL_CENTER
) {
    var size by remember { mutableStateOf(Size.Zero) }

    Image(
        painter = painterResource(id = backgroundImageId),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { layoutCoordinates ->
                size = layoutCoordinates.size.toSize()
            }
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background.copy(alpha = tintLevelCenter),
                        MaterialTheme.colorScheme.background.copy(alpha = OUTER_TINT_LEVEL),
                        MaterialTheme.colorScheme.background.copy(alpha = FULL_TINT_LEVEL)
                    ),
                    center = Offset(
                        size.width / 2,
                        size.height / 2
                    ),
                    radius = RADIAL_GRADIENT_RADIUS
                )
            )
    )
}