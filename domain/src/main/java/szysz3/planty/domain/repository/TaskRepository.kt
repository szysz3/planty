package szysz3.planty.domain.repository

import szysz3.planty.domain.model.SubTask
import szysz3.planty.domain.model.Task

interface TaskRepository {
    suspend fun getTasks(): List<Task>

    suspend fun getTaskById(taskId: Long): Task?

    suspend fun saveTask(task: Task)

    suspend fun deleteTask(task: Task)

    suspend fun updateSubTaskCompletion(taskId: Long, subTask: SubTask, isCompleted: Boolean)

    suspend fun addSubTask(taskId: Long, description: String, cost: Int = 0)
}
