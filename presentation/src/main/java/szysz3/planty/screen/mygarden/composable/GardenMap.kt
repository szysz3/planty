package szysz3.planty.screen.mygarden.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import szysz3.planty.core.model.GardenCell
import szysz3.planty.screen.mygarden.model.GardenState
import szysz3.planty.screen.mygarden.model.MergedCell

@Composable
fun GardenMap(
    modifier: Modifier = Modifier,
    rows: Int,
    columns: Int,
    state: GardenState,
    isEditMode: Boolean = false,
    selectedCells: Set<Pair<Int, Int>> = emptySet(),
    onCellClick: (Int, Int) -> Unit,
    onMergedCellClick: (MergedCell) -> Unit
) {
    val density = LocalDensity.current
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val cellSize = minOf(
        screenWidth / columns,
        100.dp
    )

    // We'll track which cells we’ve “consumed” in a merged cell
    val skipMap = mutableSetOf<Pair<Int, Int>>()

    // This Box is as large as the entire garden grid
    Box(
        modifier = modifier
            .width(cellSize * columns)
            .height(cellSize * rows)
    ) {
        for (row in 0 until rows) {
            for (col in 0 until columns) {
                val position = row to col

                // Skip if already consumed by a merged cell
                if (skipMap.contains(position)) {
                    continue
                }

                // Check if we're inside any merged cell
                val mergedCell = state.mergedCells.find { cell ->
                    row in cell.startRow..cell.endRow &&
                            col in cell.startColumn..cell.endColumn
                }

                if (mergedCell != null) {
                    // We are inside a merged cell
                    if (row == mergedCell.startRow && col == mergedCell.startColumn) {
                        // Render the merged cell only at its top-left corner
                        Box(
                            modifier = Modifier
                                // Move to the cell’s top-left offset
                                .offset(
                                    x = cellSize * mergedCell.startColumn,
                                    y = cellSize * mergedCell.startRow
                                )
                                // Size to cover the entire merged region
                                .width(cellSize * mergedCell.width)
                                .height(cellSize * mergedCell.height)
                        ) {
                            MergedCellContent(
                                mergedCell = mergedCell,
                                cellSize = cellSize,
                                width = mergedCell.width,
                                height = mergedCell.height,
                                onClick = { onMergedCellClick(mergedCell) }
                            )
                        }

                        // Mark all covered cells as consumed
                        for (r in mergedCell.startRow..mergedCell.endRow) {
                            for (c in mergedCell.startColumn..mergedCell.endColumn) {
                                skipMap.add(r to c)
                            }
                        }
                    } else {
                        // We are within the merged area, but NOT at top-left
                        skipMap.add(position)
                    }
                } else {
                    // This is a normal cell
                    val isSelected = selectedCells.contains(position)

                    // Place the single cell at its absolute (x,y) offset
                    Box(
                        modifier = Modifier
                            .offset(x = cellSize * col, y = cellSize * row)
                            .size(cellSize)
                    ) {
                        GardenCellContent(
                            cell = state.cells.find { it.row == row && it.column == col },
                            size = cellSize,
                            isSelected = isSelected,
                            isEditMode = isEditMode,
                            onClick = { onCellClick(row, col) }
                        )
                    }
                }
            }
        }
    }
}


// Existing MergedCellContent
@Composable
private fun MergedCellContent(
    mergedCell: MergedCell,
    cellSize: Dp,
    width: Int,
    height: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(cellSize * width)
            .height(cellSize * height)
            .padding(2.dp)
            .background(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                shape = RoundedCornerShape(4.dp)
            )
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(4.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (mergedCell.subGardenId != null) {
                Icon(
                    imageVector = Icons.Rounded.MoreVert,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "${width}x$height",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                Text(
                    text = "Tap to create\nsub-garden",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// Existing GardenCellContent
@Composable
private fun GardenCellContent(
    cell: GardenCell?,
    size: Dp,
    isSelected: Boolean,
    isEditMode: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(size)
            .padding(2.dp)
            .background(
                color = when {
                    isSelected && isEditMode -> MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                    cell?.plant != null -> MaterialTheme.colorScheme.secondaryContainer
                    else -> MaterialTheme.colorScheme.surface
                },
                shape = RoundedCornerShape(4.dp)
            )
            .border(
                width = if (isSelected && isEditMode) 2.dp else 1.dp,
                color = when {
                    isSelected && isEditMode -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.outline
                },
                shape = RoundedCornerShape(4.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        // Cell content
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
