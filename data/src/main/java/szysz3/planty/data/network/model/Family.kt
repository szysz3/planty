package szysz3.planty.data.network.model

data class Family(
    val scientificNameWithoutAuthor: String,
    val scientificNameAuthorship: String?,
    val scientificName: String
)