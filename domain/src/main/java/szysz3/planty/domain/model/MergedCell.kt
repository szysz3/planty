package szysz3.planty.domain.model

data class MergedCell(
    val id: Int,
    val gardenId: Int,
    val cellRange: CellRange,
    val subGardenId: Int?,
    val subGardenName: String? = null,
    val subGardenRows: Int? = null,
    val subGardenColumns: Int? = null
)