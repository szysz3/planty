package szysz3.planty.screen.plantid.composable

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import szysz3.planty.R
import szysz3.planty.core.model.Plant
import szysz3.planty.screen.plantid.model.PlantResult
import szysz3.planty.theme.dimensions

/**
 * A card component that displays plant identification results.
 *
 * @param plantResult The result containing plant details and identification confidence
 * @param onCardClick Callback invoked when the card is clicked, provides the identified plant or null
 */
@Composable
fun PlantResultCard(
    plantResult: PlantResult,
    onCardClick: (plant: Plant?) -> Unit
) {
    val dimens = MaterialTheme.dimensions()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(MaterialTheme.shapes.medium),
        elevation = CardDefaults.cardElevation(dimens.size4),
        shape = MaterialTheme.shapes.medium,
        onClick = { onCardClick(plantResult.plant) }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (plantResult.plant != null) {
                PlantCardDecorator(Modifier.align(Alignment.TopEnd))
            }

            Column(modifier = Modifier.fillMaxSize()) {
                PlantInfo(plantResult)

                Spacer(modifier = Modifier.weight(1f))

                PlantMatchingBar(
                    matchLevel = plantResult.confidence.toFloat(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimens.size8)
                        .align(Alignment.CenterHorizontally),
                    config = PlantMatchingBarConfig(
                        startColor = MaterialTheme.colorScheme.surface,
                        endColor = MaterialTheme.colorScheme.secondary
                    ),
                )
            }
        }
    }
}

@Composable
private fun PlantCardDecorator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(MaterialTheme.dimensions().size64)
            .rotate(45f)
            .offset(y = (-48).dp)
            .background(color = MaterialTheme.colorScheme.primary)
    )
}

@Composable
private fun PlantInfo(plantResult: PlantResult) {
    val dimens = MaterialTheme.dimensions()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(
                top = dimens.spacing16,
                start = dimens.spacing16,
                end = dimens.spacing16
            )
    ) {
        PlantImage(plantResult.plant)

        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = dimens.spacing16)
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
}

@Composable
private fun PlantImage(plant: Plant?) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(plant?.imageUrls?.firstOrNull())
            .crossfade(true)
            .placeholder(R.drawable.plant_placeholder)
            .error(R.drawable.plant_placeholder)
            .build(),
        contentScale = ContentScale.Crop,
        contentDescription = plant?.commonName,
        modifier = Modifier
            .size(100.dp)
            .clip(CircleShape)
    )
}