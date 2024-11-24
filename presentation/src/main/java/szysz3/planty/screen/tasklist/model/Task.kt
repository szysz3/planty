package szysz3.planty.screen.tasklist.model

data class Task(
    val title: String,
    val tasks: List<SubTask>,
    val isCompleted: Boolean = false
)