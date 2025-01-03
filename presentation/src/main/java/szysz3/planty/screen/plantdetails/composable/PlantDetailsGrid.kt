package szysz3.planty.screen.plantdetails.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import szysz3.planty.core.composable.EvenGrid
import szysz3.planty.core.composable.ImageWithTextHorizontal
import szysz3.planty.core.model.Plant
import szysz3.planty.screen.plantdetails.model.toDetailItems

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
                    .padding(4.dp)
            )
        }
    )
}
