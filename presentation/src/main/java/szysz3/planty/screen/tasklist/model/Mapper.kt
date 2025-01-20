package szysz3.planty.screen.tasklist.model

import szysz3.planty.core.model.Task

fun Task.toTaskCardUiState(): TaskCardUiState {
    val completedSubTasks = subTasks.count { it.isCompleted }
    val totalSubTasks = subTasks.size
    val completedCost = subTasks.filter { it.isCompleted }.sumOf { it.cost?.toDouble() ?: 0.0 }
    val totalCost = subTasks.sumOf { it.cost?.toDouble() ?: 0.0 }

    return TaskCardUiState(
        title = title,
        color = color,
        completedSubTasks = completedSubTasks,
        totalSubTasks = totalSubTasks,
        completedCost = completedCost,
        totalCost = totalCost,
        isCompleted = completedSubTasks == totalSubTasks
    )
}