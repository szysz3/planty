package szysz3.planty.data.network.model

data class IdentificationQuery(
    val project: String,
    val images: List<String>,
    val organs: List<String>?,
    val includeRelatedImages: Boolean,
    val noReject: Boolean
)