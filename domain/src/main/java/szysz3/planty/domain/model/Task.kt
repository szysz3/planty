package szysz3.planty.domain.model

data class Task(
    val id: Long = 0,
    val title: String,
    val subTasks: List<SubTask>,
    val isCompleted: Boolean = false,
    val color: Long? = null,
    val index: Int,
)