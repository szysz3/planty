package szysz3.planty.domain.model

data class MergedCell(
    val id: Int,
    val parentGardenId: Int?,
    val startRow: Int,
    val startColumn: Int,
    val endRow: Int,
    val endColumn: Int,
    val subGardenId: Int?
) {
    val width: Int = endColumn - startColumn + 1
    val height: Int = endRow - startRow + 1
}