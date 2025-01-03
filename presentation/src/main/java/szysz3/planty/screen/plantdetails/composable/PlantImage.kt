package szysz3.planty.screen.plantdetails.composable

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import szysz3.planty.R
import szysz3.planty.core.model.Plant

@Composable
fun PlantImage(
    plant: Plant,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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