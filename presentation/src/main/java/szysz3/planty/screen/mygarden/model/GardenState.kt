package szysz3.planty.screen.mygarden.model

import szysz3.planty.core.model.GardenCell

/**
 * Represents the state of a garden.
 * This class holds all information about a garden's layout, including its dimensions,
 * individual cells, and any merged cell configurations.
 *
 * @property id Unique identifier for the garden
 * @property name Display name of the garden
 * @property rows Number of rows in the garden grid
 * @property columns Number of columns in the garden grid
 * @property cells List of all individual garden cells, stored in row-major order
 * @property mergedCells List of merged cell configurations in the garden
 */
data class GardenState(
    val id: Int = 0,
    val name: String = "",
    val rows: Int = 0,
    val columns: Int = 0,
    val cells: List<GardenCell> = emptyList(),
    val mergedCells: List<MergedCell> = emptyList()
) {
    companion object {
        fun empty(rows: Int, columns: Int): GardenState {
            val cells = List(rows * columns) { index ->
                val row = index / columns
                val column = index % columns
                GardenCell(id = 0, row = row, column = column, plant = null)
            }
            return GardenState(rows = rows, columns = columns, cells = cells, name = "")
        }
    }
}