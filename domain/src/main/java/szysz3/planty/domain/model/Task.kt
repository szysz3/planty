package szysz3.planty.domain.model

data class Task(
    val id: Long = 0,
    val title: String,
    val tasks: List<SubTask>,
    val isCompleted: Boolean = false,
    val color: Long? = null,
    val index: Int,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Task) return false

        return id == other.id &&
                title == other.title &&
                isCompleted == other.isCompleted &&
                color == other.color &&
                index == other.index &&
                tasks.size == other.tasks.size
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + isCompleted.hashCode()
        result = 31 * result + (color?.hashCode() ?: 0)
        result = 31 * result + index
        result = 31 * result + tasks.size
        return result
    }
}