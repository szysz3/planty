package szysz3.planty.screen.home.model

data class GardenState(
    val rows: Int = 0,
    val columns: Int = 0,
    val cells: List<GardenCell> = emptyList()
) {

    companion object {
        fun empty(rows: Int, columns: Int): GardenState {
            val cells = List(rows * columns) { index ->
                val row = index / columns
                val column = index % columns
                GardenCell(id = 0, row = row, column = column, plant = null)
            }
            return GardenState(rows = rows, columns = columns, cells = cells)
        }
    }
}

