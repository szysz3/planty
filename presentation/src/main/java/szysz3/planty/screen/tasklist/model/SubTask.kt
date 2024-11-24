package szysz3.planty.screen.tasklist.model

data class SubTask(
    val description: String,
    val isCompleted: Boolean = false,
    val cost: Int = 0
)