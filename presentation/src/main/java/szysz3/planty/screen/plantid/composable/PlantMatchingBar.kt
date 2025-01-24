package szysz3.planty.screen.plantid.composable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import szysz3.planty.theme.dimensions

/**
 * Configuration options for customizing the appearance of [PlantMatchingBar].
 *
 * @property startColor The color representing low match level
 * @property endColor The color representing high match level
 * @property backgroundColor The background color of the bar
 */
data class PlantMatchingBarConfig(
    val startColor: Color = Color.Transparent,
    val endColor: Color = Color.Transparent,
    val backgroundColor: Color = Color.Transparent
)

/**
 * A composable that displays a horizontal progress bar indicating the plant matching level.
 * The bar color transitions between configured colors based on the matching level.
 *
 * @param matchLevel The matching confidence level between 0.0 and 1.0
 * @param modifier Optional modifier for customizing the layout
 * @param config Configuration options for customizing the appearance
 */
@Composable
fun PlantMatchingBar(
    matchLevel: Float,
    modifier: Modifier = Modifier,
    config: PlantMatchingBarConfig = PlantMatchingBarConfig()
) {
    val cornerRadius = MaterialTheme.dimensions().corner16.value
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                color = config.backgroundColor
            )
            val barWidth = size.width * matchLevel
            drawRoundRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(config.startColor, config.endColor),
                    startX = 0f,
                    endX = barWidth
                ),
                size = size.copy(width = barWidth),
                topLeft = Offset(0f, 0f),
                cornerRadius = CornerRadius(
                    cornerRadius,
                    cornerRadius
                )
            )
        }
    }
}