package szysz3.planty.screen.mygarden.model

import szysz3.planty.screen.plantcatalog.model.Plant

data class GardenCell(
    val id: Int,
    val row: Int,
    val column: Int,
    val plant: Plant?
)