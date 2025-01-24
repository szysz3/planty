package szysz3.planty.screen.plantdetails.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import szysz3.planty.core.composable.EvenGrid
import szysz3.planty.core.composable.ImageWithTextHorizontal
import szysz3.planty.core.model.Plant
import szysz3.planty.screen.plantdetails.model.toDetailItems
import szysz3.planty.theme.dimensions

/**
 * A grid-based display of plant details, showing various attributes of a plant in a 2-column layout.
 * Each grid item contains an icon and associated text representing a plant characteristic.
 *
 * @param plant The plant model containing the details to be displayed
 * @param modifier Optional modifier for customizing the layout of individual grid items
 */
@Composable
fun PlantDetailsGrid(
    plant: Plant,
    modifier: Modifier = Modifier
) {
    val elements = plant.toDetailItems()
    EvenGrid(
        items = elements,
        columns = 2,
        modifier = Modifier.fillMaxWidth(),
        createItem = { item ->
            ImageWithTextHorizontal(
                imageRes = item.imageRes,
                title = item.title,
                modifier = modifier
                    .padding(MaterialTheme.dimensions().spacing4)
            )
        }
    )
}
