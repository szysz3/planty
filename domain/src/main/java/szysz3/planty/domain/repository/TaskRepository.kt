package szysz3.planty.domain.repository

import kotlinx.coroutines.flow.Flow
import szysz3.planty.domain.model.SubTask
import szysz3.planty.domain.model.Task

interface TaskRepository {
    val tasksFlow: Flow<List<Task>>

    suspend fun saveTask(task: Task)

    suspend fun deleteTask(task: Task)

    suspend fun updateSubTaskCompletion(taskId: Long, subTask: SubTask, isCompleted: Boolean)

    suspend fun addSubTask(taskId: Long, description: String, cost: Int = 0)
}
