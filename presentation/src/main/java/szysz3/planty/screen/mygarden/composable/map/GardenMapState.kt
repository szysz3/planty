package szysz3.planty.screen.mygarden.composable.map

import szysz3.planty.screen.mygarden.model.CellPosition
import szysz3.planty.screen.mygarden.model.GardenState

/**
 * Represents the state of the garden map grid.
 *
 * @property rows Number of rows in the garden grid
 * @property columns Number of columns in the garden grid
 * @property gardenState Current state of the garden including merged cells and other properties
 * @property isEditMode Whether the garden is currently in edit mode
 * @property selectedCells Set of currently selected cell positions in the garden
 */
data class GardenMapState(
    val rows: Int,
    val columns: Int,
    val gardenState: GardenState,
    val isEditMode: Boolean = false,
    val selectedCells: Set<CellPosition> = emptySet()
) {
    /**
     * Checks if a cell at the specified position is currently selected.
     *
     * @param row Row index to check
     * @param column Column index to check
     * @return true if the cell is selected, false otherwise
     */
    fun isCellSelected(row: Int, column: Int): Boolean {
        return selectedCells.contains(CellPosition(row, column))
    }
}