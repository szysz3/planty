package szysz3.planty.screen.plantdetails.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import szysz3.planty.core.model.Plant
import szysz3.planty.screen.plantdetails.model.hasAdditionalInfo
import szysz3.planty.theme.dimensions

/**
 * A horizontal section displaying a clickable plant image and optional additional information.
 *
 * @param plant The plant model containing the image and additional information
 * @param onImageClick Callback invoked when the plant image is clicked
 * @param modifier Optional modifier for customizing the section's layout
 */
@Composable
fun PlantImageSection(
    plant: Plant,
    onImageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dimens = MaterialTheme.dimensions()
    Row(
        modifier = modifier
            .height(200.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PlantImage(
            plant = plant,
            modifier = Modifier
                .weight(1.3f)
                .clickable(onClick = onImageClick),
        )

        if (plant.hasAdditionalInfo()) {
            Spacer(modifier = Modifier.width(dimens.size8))
            PlantAdditionalInfoSection(
                plant = plant,
                modifier = Modifier
                    .weight(0.7f)
            )
        }
    }
}