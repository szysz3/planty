package szysz3.planty.ui.widgets

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.toSize

@Composable
fun EllipticalBackground(@DrawableRes backgroundImageId: Int) {
    var size by remember { mutableStateOf(Size.Zero) }

    Image(
        painter = painterResource(id = backgroundImageId), // Replace with your background image
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop // Adjust as needed (Crop, Fit, FillBounds, etc.)
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
                        Color.Transparent,  // Transparent in the center
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),  // Gradual fade-out effect
                        MaterialTheme.colorScheme.surface  // Fully solid on the edges
                    ),
                    center = Offset(
                        size.width / 2,
                        size.height / 2
                    ), // Center point of the gradient
                    radius = 900f // Control the spread of the gradient
                )
            )
    )
}