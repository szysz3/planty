package szysz3.planty.domain.model

data class GardenState(
    val id: Int,
    val rows: Int = 0,
    val name: String = "",
    val columns: Int = 0,
    val cells: List<GardenCell> = emptyList(),
    val mergedCells: List<MergedCell> = emptyList()  // Using domain MergedCell
)