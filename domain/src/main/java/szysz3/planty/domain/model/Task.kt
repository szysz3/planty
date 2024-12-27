package szysz3.planty.domain.model

data class Task(
    val id: Long = 0,
    val title: String,
    val tasks: List<SubTask>,
    val isCompleted: Boolean = false,
    val color: Long? = null
)