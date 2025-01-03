package szysz3.planty.screen.plantdetails.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import szysz3.planty.core.model.Plant
import szysz3.planty.screen.plantdetails.model.hasAdditionalInfo

@Composable
fun PlantImageSection(
    plant: Plant,
    onImageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(200.dp), // Fixed height to match original
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Plant Image with weight
        PlantImage(
            plant = plant,
            modifier = Modifier
                .weight(1.3f)
                .clickable(onClick = onImageClick),
        )

        if (plant.hasAdditionalInfo()) {
            Spacer(modifier = Modifier.width(8.dp))
            PlantAdditionalInfoSection(
                plant = plant,
                modifier = Modifier
                    .weight(0.7f)
            )
        }
    }
}