package szysz3.planty.screen.tasklist.model

data class SubTask(
    val id: Long = 0,
    val description: String = "",
    val isCompleted: Boolean = false,
    val cost: Int = 0
)