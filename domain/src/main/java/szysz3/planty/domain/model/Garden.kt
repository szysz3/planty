package szysz3.planty.domain.model

data class Garden(
    val id: Int,
    val name: String,
    val parentGardenId: Int?,
    val rows: Int,
    val columns: Int
)