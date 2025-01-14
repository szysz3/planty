package szysz3.planty.domain.model

data class CellRange(
    val startRow: Int,
    val startColumn: Int,
    val endRow: Int,
    val endColumn: Int
) {
    init {
        require(startRow <= endRow) { "startRow must be less than or equal to endRow" }
        require(startColumn <= endColumn) { "startColumn must be less than or equal to endColumn" }
    }
}