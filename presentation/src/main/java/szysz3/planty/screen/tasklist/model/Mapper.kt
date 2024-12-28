package szysz3.planty.screen.tasklist.model

import androidx.compose.ui.graphics.Color
import szysz3.planty.domain.model.SubTask as SubTaskDomain
import szysz3.planty.domain.model.Task as TaskDomain

fun SubTaskDomain.toPresentation(): SubTask {
    return SubTask(
        id = this.id,
        description = this.description,
        isCompleted = this.isCompleted,
        cost = this.cost
    )
}

fun TaskDomain.toPresentation(): Task {
    return Task(
        id = this.id,
        title = this.title,
        tasks = this.subTasks.map { it.toPresentation() },
        isCompleted = this.isCompleted,
        color = Color(color ?: 0),
        index = this.index
    )
}

fun List<TaskDomain>.toPresentation(): List<Task> {
    return map { it.toPresentation() }
}

fun List<Task>.toDomain(): List<TaskDomain> {
    return map { it.toDomain() }
}

fun SubTask.toDomain(): SubTaskDomain {
    return SubTaskDomain(
        id = this.id,
        description = this.description,
        isCompleted = this.isCompleted,
        cost = this.cost
    )
}

fun Task.toDomain(): TaskDomain {
    return TaskDomain(
        id = this.id,
        title = this.title,
        subTasks = this.tasks.map { it.toDomain() },
        isCompleted = this.isCompleted,
        color = this.color.value.toLong(),
        index = this.index
    )
}