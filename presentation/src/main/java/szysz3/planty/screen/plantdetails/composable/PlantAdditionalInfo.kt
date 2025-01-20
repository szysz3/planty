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

/**
 * A composable that displays additional information about a plant including its width, height,
 * and growth rate. Each piece of information is displayed with a corresponding icon.
 *
 * @param plant The plant model containing the information to be displayed
 * @param modifier Optional modifier for customizing the layout
 */
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