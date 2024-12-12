package szysz3.planty.domain.model.remote

import szysz3.planty.domain.model.Plant

data class Result(
    val score: Double,
    val species: Species,
    val plant: Plant?,
    val gbif: Map<String, String>?,
    val powo: Map<String, String>?,
    val iucn: Map<String, String>?
)
