package szysz3.planty.domain.model

data class SubTask(
    val id: Long = 0,
    val description: String,
    val isCompleted: Boolean = false,
    val cost: Float = 0f
)