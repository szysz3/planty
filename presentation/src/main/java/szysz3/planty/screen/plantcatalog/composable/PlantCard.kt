package szysz3.planty.screen.plantcatalog.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import coil.request.ImageRequest
import szysz3.planty.R
import szysz3.planty.core.model.Plant
import szysz3.planty.theme.dimensions

private object PlantCardDefaults {
    const val CARD_ASPECT_RATIO = 0.8f
    const val IMAGE_HEIGHT_FRACTION = 0.7f
}

/**
 * A composable that displays a card containing plant information with an image and text details.
 *
 * @param modifier Modifier to be applied to the card
 * @param plant Plant data to be displayed. If null, placeholder content will be shown
 * @param onPlantSelected Callback triggered when the card is clicked. If null, the card won't be clickable
 */
@Composable
fun PlantCard(
    modifier: Modifier = Modifier,
    plant: Plant? = null,
    onPlantSelected: (() -> Unit)? = null
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(PlantCardDefaults.CARD_ASPECT_RATIO)
            .then(onPlantSelected?.let { Modifier.clickable(onClick = it) } ?: Modifier),
        elevation = CardDefaults.cardElevation(
            defaultElevation = MaterialTheme.dimensions().elevation4
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.dimensions().spacing8)
        ) {
            PlantImage(plant)
            Spacer(modifier = Modifier.height(MaterialTheme.dimensions().spacing8))
            PlantDetails(plant)
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
            .fillMaxHeight(PlantCardDefaults.IMAGE_HEIGHT_FRACTION)
            .aspectRatio(1f)
            .clip(CircleShape)
    )
}

@Composable
private fun PlantDetails(plant: Plant?) {
    Text(
        text = plant?.latinName ?: "",
        textAlign = TextAlign.Center,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.fillMaxWidth()
    )

    if (plant?.commonName?.isNotBlank() == true) {
        Text(
            text = plant.commonName,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}