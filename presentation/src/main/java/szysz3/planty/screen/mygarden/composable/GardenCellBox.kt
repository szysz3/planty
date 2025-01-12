package szysz3.planty.screen.mygarden.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
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