package szysz3.planty.screen.tasklist.model

data class TaskListScreenState(
    val tasks: List<Task> = emptyList()
) {
    val totalCost: Int
        get() = tasks.filterNot { it.isCompleted }
            .flatMap { it.tasks }
            .filterNot { it.isCompleted }
            .sumOf { it.cost }
}