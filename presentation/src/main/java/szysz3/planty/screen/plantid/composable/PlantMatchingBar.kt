package szysz3.planty.screen.plantid.composable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun PlantMatchingBar(
    matchLevel: Float,
    modifier: Modifier = Modifier,
    cornerRadius: Float = 16f,
    startColor: Color = Color.Red,
    endColor: Color = Color.Green,
    backgroundColor: Color = Color.Transparent
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                color = backgroundColor,
            )
            val barWidth = size.width * matchLevel
            drawRoundRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(startColor, endColor),
                    startX = 0f,
                    endX = barWidth
                ),
                size = size.copy(width = barWidth),
                topLeft = Offset(0f, 0f),
                cornerRadius = CornerRadius(cornerRadius, cornerRadius)
            )
        }
    }
}