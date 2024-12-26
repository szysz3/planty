package szysz3.planty.screen.plantid.composable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import szysz3.planty.R
import szysz3.planty.screen.plantcatalog.model.Plant
import szysz3.planty.screen.plantid.model.PlantResult

@Composable
fun PlantResultCard(plantResult: PlantResult, onCardClick: (plant: Plant?) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(MaterialTheme.shapes.medium),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = MaterialTheme.shapes.medium,
        onClick = {
            onCardClick(plantResult.plant)
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (plantResult.plant != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(48.dp)
                        .rotate(45f)
                        .offset(y = (-48).dp)
                        .background(color = MaterialTheme.colorScheme.primary)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.plant_placeholder),
                        contentDescription = "plant image placeholder",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .align(Alignment.CenterVertically)
                    )

                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 16.dp)
                    ) {
                        plantResult.name?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.titleMedium,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        plantResult.scientificName?.let {
                            Text(
                                text = "($it)",
                                style = MaterialTheme.typography.bodyMedium,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                PlantMatchingBar(
                    matchLevel = plantResult.confidence.toFloat(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .align(Alignment.CenterHorizontally),
                    startColor = MaterialTheme.colorScheme.surface,
                    endColor = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

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