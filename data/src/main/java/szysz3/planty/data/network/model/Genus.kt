package szysz3.planty.data.network.model

data class Genus(
    val scientificNameWithoutAuthor: String,
    val scientificNameAuthorship: String?,
    val scientificName: String
)