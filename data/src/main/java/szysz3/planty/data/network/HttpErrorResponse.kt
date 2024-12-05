package szysz3.planty.data.network

data class HttpErrorResponse(
    val statusCode: Int,
    val error: String,
    val message: String
)