package szysz3.planty.screen.mygarden.composable.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import szysz3.planty.screen.mygarden.composable.GardenCellBox
import szysz3.planty.screen.mygarden.composable.GardenMergedCellBox
import szysz3.planty.screen.mygarden.model.CellPosition
import szysz3.planty.screen.mygarden.model.MergedCell

private const val MAX_CELL_SIZE = 100

/**
 * A composable that displays an interactive garden map grid with support for merged cells.
 * The map automatically scales based on available width while maintaining cell proportions.
 *
 * @param modifier Modifier to be applied to the garden map container
 * @param state Current state of the garden map, containing grid dimensions and cell states
 * @param callbacks Interface for handling user interactions with the garden map
 */
@Composable
fun GardenMap(
    modifier: Modifier = Modifier,
    state: GardenMapState,
    callbacks: GardenMapCallbacks
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val cellSize = minOf(maxWidth / state.columns, MAX_CELL_SIZE.dp)
        GardenGrid(
            state = state,
            callbacks = callbacks,
            cellSize = cellSize
        )
    }
}

@Composable
private fun BoxWithConstraintsScope.GardenGrid(
    state: GardenMapState,
    callbacks: GardenMapCallbacks,
    cellSize: Dp
) {
    val processedCells = mutableSetOf<CellPosition>()
    Box(
        modifier = Modifier
            .width(cellSize * state.columns)
            .height(cellSize * state.rows)
            .align(Alignment.Center)
    ) {
        for (row in 0 until state.rows) {
            for (col in 0 until state.columns) {
                val position = CellPosition(row, col)
                if (position in processedCells) continue

                val mergedCell = findMergedCell(state.gardenState.mergedCells, row, col)

                when {
                    mergedCell != null && position == CellPosition(
                        mergedCell.startRow,
                        mergedCell.startColumn
                    ) -> {
                        GardenMergedCellBox(
                            mergedCell = mergedCell,
                            cellSize = cellSize,
                            onClick = { callbacks.onMergedCellClick(mergedCell) }
                        )
                        markMergedCells(mergedCell, processedCells)
                    }

                    mergedCell != null -> {
                        processedCells.add(position)
                    }

                    else -> {
                        GardenCellBox(
                            position = CellPosition(row, col),
                            cellSize = cellSize,
                            mapState = state,
                            onClick = { callbacks.onCellClick(row, col) }
                        )
                    }
                }
            }
        }
    }
}

private fun findMergedCell(
    mergedCells: List<MergedCell>,
    row: Int,
    col: Int
): MergedCell? = mergedCells.find { cell ->
    row in cell.startRow..cell.endRow &&
            col in cell.startColumn..cell.endColumn
}

private fun markMergedCells(
    mergedCell: MergedCell,
    processedCells: MutableSet<CellPosition>
) {
    for (r in mergedCell.startRow..mergedCell.endRow) {
        for (c in mergedCell.startColumn..mergedCell.endColumn) {
            processedCells.add(CellPosition(r, c))
        }
    }
}