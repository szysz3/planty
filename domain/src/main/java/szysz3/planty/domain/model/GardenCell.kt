package szysz3.planty.domain.model

data class GardenCell(
    val id: Int,
    val row: Int,
    val column: Int,
    val plant: Plant?
)