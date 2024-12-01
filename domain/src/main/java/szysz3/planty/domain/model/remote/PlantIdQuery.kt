package szysz3.planty.domain.model.remote

data class IdentificationQuery(
    val project: String,
    val images: List<String>,
    val organs: List<String>?,
    val includeRelatedImages: Boolean,
    val noReject: Boolean
)