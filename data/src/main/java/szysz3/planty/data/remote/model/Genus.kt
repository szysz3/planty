package szysz3.planty.data.remote.model

data class Genus(
    val scientificNameWithoutAuthor: String,
    val scientificNameAuthorship: String?,
    val scientificName: String
)