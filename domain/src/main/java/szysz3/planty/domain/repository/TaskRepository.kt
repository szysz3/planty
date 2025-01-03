package szysz3.planty.domain.repository

import kotlinx.coroutines.flow.Flow
import szysz3.planty.domain.model.SubTask
import szysz3.planty.domain.model.Task

interface TaskRepository {
    suspend fun getTasksWithSubTasksFlow(): Flow<List<Task>>

    suspend fun getTasks(): List<Task>

    suspend fun getTaskById(taskId: Long): Task?

    suspend fun saveTask(task: Task)

    suspend fun saveTasks(tasks: List<Task>)

    suspend fun deleteTask(task: Task)

    suspend fun updateSubTaskCompletion(taskId: Long, subTask: SubTask, isCompleted: Boolean)

    suspend fun updateTasks(tasks: List<Task>)

    suspend fun updateTask(task: Task)

    suspend fun addSubTask(taskId: Long, description: String, cost: Int)
}
