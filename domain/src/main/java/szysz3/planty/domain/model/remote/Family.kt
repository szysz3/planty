package szysz3.planty.domain.model.remote

data class Family(
    val scientificNameWithoutAuthor: String,
    val scientificNameAuthorship: String?,
    val scientificName: String
)