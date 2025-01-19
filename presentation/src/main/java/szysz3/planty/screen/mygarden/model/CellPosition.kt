package szysz3.planty.screen.mygarden.model

/**
 * Represents a position in the garden grid.
 *
 * @property row The row index in the garden grid (0-based)
 * @property column The column index in the garden grid (0-based)
 */
data class CellPosition(
    val row: Int,
    val column: Int
) {
    companion object {
        fun from(row: Int, column: Int) = CellPosition(row, column)
    }
}