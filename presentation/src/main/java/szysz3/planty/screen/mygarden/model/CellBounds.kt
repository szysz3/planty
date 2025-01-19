package szysz3.planty.screen.mygarden.model

/**
 * Represents the boundaries of a rectangular cell selection in a grid.
 * All coordinates are inclusive.
 */
data class CellBounds(
    val minRow: Int,
    val minCol: Int,
    val maxRow: Int,
    val maxCol: Int
) {
    companion object {
        fun from(cells: Set<CellPosition>): CellBounds {
            require(cells.isNotEmpty()) { "Cell set cannot be empty" }
            return CellBounds(
                minRow = cells.minOf { it.row },
                minCol = cells.minOf { it.column },
                maxRow = cells.maxOf { it.row },
                maxCol = cells.maxOf { it.column }
            )
        }
    }

    private fun isRowInBounds(row: Int): Boolean = row in minRow..maxRow

    private fun isColumnInBounds(column: Int): Boolean = column in minCol..maxCol

    fun isValidRectangularSelection(cells: Set<CellPosition>): Boolean {
        val width = maxCol - minCol + 1
        val height = maxRow - minRow + 1
        val expectedSize = width * height

        return cells.size == expectedSize &&
                cells.all { cell ->
                    isRowInBounds(cell.row) && isColumnInBounds(cell.column)
                }
    }
}