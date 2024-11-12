package szysz3.planty.screen.home.model

import szysz3.planty.domain.model.GardenCell

data class GardenState(
    val cells: List<GardenCell>?,
    val dimensions: MapDimensions?
)