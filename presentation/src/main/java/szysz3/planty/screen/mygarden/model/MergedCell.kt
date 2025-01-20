package szysz3.planty.screen.mygarden.model

/**
 * Represents a merged cell within a garden grid that can span multiple rows and columns.
 * A merged cell can either be a simple merged area or contain a sub-garden, allowing for
 * nested garden structures.
 *
 * @property id Unique identifier for the merged cell
 * @property parentGardenId ID of the garden containing this merged cell
 * @property startRow Starting row index of the merged cell in the parent garden grid
 * @property startColumn Starting column index of the merged cell in the parent garden grid
 * @property endRow Ending row index of the merged cell in the parent garden grid
 * @property endColumn Ending column index of the merged cell in the parent garden grid
 * @property subGardenId Optional ID of a nested garden contained within this merged cell
 * @property subGardenName Optional name of the nested garden
 * @property subGardenRows Optional number of rows in the nested garden
 * @property subGardenColumns Optional number of columns in the nested garden
 * @property width Calculated width of the merged cell (derived from endColumn - startColumn + 1)
 * @property height Calculated height of the merged cell (derived from endRow - startRow + 1)
 */
data class MergedCell(
    val id: Int,
    val parentGardenId: Int?,
    val startRow: Int,
    val startColumn: Int,
    val endRow: Int,
    val endColumn: Int,
    val subGardenId: Int?,
    val subGardenName: String? = null,
    val subGardenRows: Int? = null,
    val subGardenColumns: Int? = null
) {
    val width: Int = endColumn - startColumn + 1
    val height: Int = endRow - startRow + 1
}