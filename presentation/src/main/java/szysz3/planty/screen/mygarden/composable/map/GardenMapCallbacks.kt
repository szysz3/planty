package szysz3.planty.screen.mygarden.composable.map

import szysz3.planty.screen.mygarden.model.MergedCell

/**
 * Contains callback functions for handling user interactions with the garden map.
 *
 * @property onCellClick Callback invoked when a regular cell is clicked.
 *                      Parameters: row (Int) - The row index of the clicked cell
 *                                 col (Int) - The column index of the clicked cell
 * @property onMergedCellClick Callback invoked when a merged cell is clicked.
 *                             Parameter: mergedCell (MergedCell) - The clicked merged cell containing its dimensions and properties
 */
data class GardenMapCallbacks(
    val onCellClick: (Int, Int) -> Unit,
    val onMergedCellClick: (MergedCell) -> Unit
)