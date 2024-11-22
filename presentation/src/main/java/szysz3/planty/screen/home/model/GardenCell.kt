package szysz3.planty.screen.home.model

import szysz3.planty.screen.plantaplant.model.Plant

data class GardenCell(
    val id: Int,
    val row: Int,
    val column: Int,
    val plant: Plant?
)