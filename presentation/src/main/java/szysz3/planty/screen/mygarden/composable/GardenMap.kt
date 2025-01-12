package szysz3.planty.screen.mygarden.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
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
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val cellSize = minOf(screenWidth / columns, 100.dp)
    val skipMap = mutableSetOf<Pair<Int, Int>>()

    Box(
        modifier = modifier
            .width(cellSize * columns)
            .height(cellSize * rows)
    ) {
        for (row in 0 until rows) {
            for (col in 0 until columns) {
                val position = row to col
                if (position in skipMap) continue

                val mergedCell = state.mergedCells.find { cell ->
                    row in cell.startRow..cell.endRow && col in cell.startColumn..cell.endColumn
                }

                if (mergedCell != null) {
                    if (row == mergedCell.startRow && col == mergedCell.startColumn) {
                        GardenMergedCellBox(
                            mergedCell = mergedCell,
                            cellSize = cellSize,
                            onClick = { onMergedCellClick(mergedCell) }
                        )
                        markMergedCells(mergedCell, skipMap)
                    } else {
                        skipMap.add(position)
                    }
                } else {
                    val isSelected = position in selectedCells
                    GardenCellBox(
                        row = row,
                        col = col,
                        cellSize = cellSize,
                        isSelected = isSelected,
                        isEditMode = isEditMode,
                        state = state,
                        onClick = { onCellClick(row, col) }
                    )
                }
            }
        }
    }
}

private fun markMergedCells(mergedCell: MergedCell, skipMap: MutableSet<Pair<Int, Int>>) {
    for (r in mergedCell.startRow..mergedCell.endRow) {
        for (c in mergedCell.startColumn..mergedCell.endColumn) {
            skipMap.add(r to c)
        }
    }
}

