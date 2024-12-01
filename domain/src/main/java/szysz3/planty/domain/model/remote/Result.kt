package szysz3.planty.domain.model.remote

data class Result(
    val score: Double,
    val species: Species,
    val gbif: Map<String, String>?,
    val powo: Map<String, String>?,
    val iucn: Map<String, String>?
)
