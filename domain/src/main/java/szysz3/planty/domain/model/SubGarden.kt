package szysz3.planty.domain.model

data class SubGarden(
    val id: Int,
    val parentMergedCellId: Int,
    val rows: Int,
    val columns: Int
)