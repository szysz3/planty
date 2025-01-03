package szysz3.planty.screen.plantdetails.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import szysz3.planty.R
import szysz3.planty.core.composable.ImageWithTextHorizontal
import szysz3.planty.core.model.Plant
import szysz3.planty.screen.plantdetails.model.mapGrowthRateToString

@Composable
fun PlantAdditionalInfoSection(
    plant: Plant,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        plant.width?.let { width ->
            ImageWithTextHorizontal(
                imageRes = R.drawable.icon_width,
                title = width.toString()
            )
        }

        plant.height?.let { height ->
            ImageWithTextHorizontal(
                imageRes = R.drawable.icon_height,
                title = height.toString()
            )
        }

        plant.growthRate?.let { growthRate ->
            ImageWithTextHorizontal(
                imageRes = R.drawable.icon_growth_rate,
                title = mapGrowthRateToString(growthRate)
            )
        }
    }
}