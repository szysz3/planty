package szysz3.planty.domain.model

data class MergedCell(
    val id: Int,
    val gardenId: Int,
    val cellRange: CellRange,
    val subGardenId: Int?
)