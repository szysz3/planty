package szysz3.planty.screen.plantdetails.composable

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import szysz3.planty.R
import szysz3.planty.core.model.Plant
import szysz3.planty.theme.dimensions

/**
 * Displays a plant image within card with elevation.
 *
 * @param plant The plant model containing image URLs and common name for content description
 * @param modifier Optional modifier for customizing the card's layout
 */
@Composable
fun PlantImage(
    plant: Plant,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(
            defaultElevation = MaterialTheme.dimensions().elevation4
        )
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(plant.imageUrls?.firstOrNull())
                .crossfade(true)
                .placeholder(R.drawable.plant_placeholder)
                .error(R.drawable.plant_placeholder)
                .build(),
            contentScale = ContentScale.Crop,
            contentDescription = plant.commonName,
            modifier = Modifier.fillMaxSize()
        )
    }
}