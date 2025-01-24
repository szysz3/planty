package szysz3.planty.screen.mygarden.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import szysz3.planty.R
import szysz3.planty.core.model.Plant
import szysz3.planty.screen.mygarden.composable.map.GardenMapState
import szysz3.planty.screen.mygarden.model.CellPosition
import szysz3.planty.theme.Shapes

private val DesaturatedColorMatrix = ColorMatrix().apply { setToSaturation(0.3f) }

/**
 * A composable that represents a single cell in the garden grid.
 * Each cell can either be empty or contain a plant, and can be selected in edit mode.
 *
 * @param position The position of the cell in the garden grid
 * @param cellSize The size of the cell in dp
 * @param mapState The current state of the garden map, including cell contents and selection state
 * @param onClick Callback invoked when the cell is clicked
 */
@Composable
fun GardenCellBox(
    position: CellPosition,
    cellSize: Dp,
    mapState: GardenMapState,
    onClick: () -> Unit
) {
    val cell =
        mapState.gardenState.cells.find {
            it.row == position.row
                    && it.column == position.column
        }
    val isSelected = mapState.isCellSelected(position.row, position.column)

    Box(
        modifier = Modifier
            .offset(x = cellSize * position.column, y = cellSize * position.row)
            .size(cellSize)
            .padding(2.dp)
            .background(
                color = when {
                    isSelected && mapState.isEditMode -> MaterialTheme.colorScheme.secondary.copy(
                        alpha = 0.2f
                    )

                    cell?.plant != null -> MaterialTheme.colorScheme.secondaryContainer
                    else -> MaterialTheme.colorScheme.surface
                },
                shape = Shapes.extraSmall
            )
            .border(
                width = if (isSelected && mapState.isEditMode) 2.dp else 1.dp,
                color = if (isSelected && mapState.isEditMode)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.outline,
                shape = Shapes.extraSmall
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        cell?.plant?.let { plant ->
            PlantContent(
                plant = plant,
                isHighlighted = isSelected && mapState.isEditMode
            )
        }
    }
}

@Composable
private fun PlantContent(
    plant: Plant,
    isHighlighted: Boolean
) {
    if (plant.imageUrls?.isNotEmpty() == true) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(plant.imageUrls.first())
                .crossfade(true)
                .placeholder(R.drawable.plant_placeholder)
                .error(R.drawable.plant_placeholder)
                .build(),
            contentScale = ContentScale.Crop,
            contentDescription = plant.commonName,
            colorFilter = ColorFilter.colorMatrix(DesaturatedColorMatrix),
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(1f)
                .clip(Shapes.extraSmall)
        )
    } else {
        Text(
            text = plant.commonName ?: plant.latinName,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = if (isHighlighted)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.onSurface
        )
    }
}