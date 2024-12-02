package szysz3.planty.data.network.model

data class Result(
    val score: Double,
    val species: Species,
    val gbif: Map<String, String>?,
    val powo: Map<String, String>?,
    val iucn: Map<String, String>?
)
