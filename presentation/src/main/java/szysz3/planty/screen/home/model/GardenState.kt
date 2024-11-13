package szysz3.planty.screen.home.model

data class GardenState(
    val rows: Int = 0,
    val columns: Int = 0,
    val cells: List<GardenCell> = emptyList() // List of cells with explicit row, column, and plant data
)