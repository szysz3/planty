package szysz3.planty.screen.plantid.model

import szysz3.planty.screen.plantcatalog.model.Plant

data class PlantResult(
    val name: String?,
    val scientificName: String?,
    val confidence: Double,
    val plant: Plant?
)