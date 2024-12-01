package szysz3.planty.data.remote.model

data class PlantIdResponse(
    val query: IdentificationQuery,
    val language: String,
    val preferedReferential: String,
    val bestMatch: String?,
    val results: List<Result>,
    val version: String,
    val remainingIdentificationRequests: Int
)
