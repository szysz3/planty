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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import szysz3.planty.screen.mygarden.model.GardenState

@Composable
fun GardenCellBox(
    row: Int,
    col: Int,
    cellSize: Dp,
    isSelected: Boolean,
    isEditMode: Boolean,
    state: GardenState,
    onClick: () -> Unit
) {
    val cell = state.cells.find { it.row == row && it.column == col }
    Box(
        modifier = Modifier
            .offset(x = cellSize * col, y = cellSize * row)
            .size(cellSize)
            .padding(2.dp)
            .background(
                color = when {
                    isSelected && isEditMode -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
                    cell?.plant != null -> MaterialTheme.colorScheme.secondaryContainer
                    else -> MaterialTheme.colorScheme.surface
                },
                shape = RoundedCornerShape(4.dp)
            )
            .border(
                width = if (isSelected && isEditMode) 2.dp else 1.dp,
                color = if (isSelected && isEditMode)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(4.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        cell?.plant?.let { plant ->
            if (plant.imageUrls?.isNotEmpty() == true) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(plant.imageUrls.firstOrNull())
                        .crossfade(true)
                        .placeholder(R.drawable.plant_placeholder)
                        .error(R.drawable.plant_placeholder)
                        .build(),
                    contentScale = ContentScale.Crop,
                    contentDescription = plant.commonName,
                    colorFilter = ColorFilter.colorMatrix(
                        ColorMatrix().apply {
                            setToSaturation(0.3f)
                        }
                    ),
                    modifier = Modifier
                        .fillMaxSize()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(4.dp))
                )
            } else {
                Text(
                    text = plant.commonName ?: plant.latinName,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = if (isSelected && isEditMode)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}