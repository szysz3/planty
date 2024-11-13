package szysz3.planty.domain.model

data class GardenState(
    val rows: Int = 0,
    val columns: Int = 0,
    val cells: List<GardenCell> = emptyList()
)