package szysz3.planty.data.repository

import szysz3.planty.data.database.dao.TaskDao
import szysz3.planty.data.database.entity.SubTaskEntity
import szysz3.planty.data.database.entity.toDomain
import szysz3.planty.data.database.entity.toEntity
import szysz3.planty.domain.model.SubTask
import szysz3.planty.domain.model.Task
import szysz3.planty.domain.repository.TaskRepository
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(private val taskDao: TaskDao) : TaskRepository {

    override suspend fun getTasks(): List<Task> {
        val tasksWithSubTasks = taskDao.getTasksWithSubTasks()
        return tasksWithSubTasks.map { it.toDomain() }
    }

    override suspend fun getTaskById(taskId: Long): Task? {
        val taskWithSubTasks = taskDao.getTasksWithSubTasks(taskId)
        return taskWithSubTasks?.toDomain()
    }

    override suspend fun saveTask(task: Task) {
        val (taskEntity, subTaskEntities) = task.toEntity()
        val taskId = taskDao.insertTask(taskEntity)
        val updatedSubTaskEntities = subTaskEntities.map { it.copy(taskId = taskId) }
        taskDao.insertSubTasks(updatedSubTaskEntities)
    }

    override suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task.toEntity().first)
    }

    override suspend fun updateSubTaskCompletion(
        taskId: Long,
        subTask: SubTask,
        isCompleted: Boolean
    ) {
        val subTaskEntity = SubTaskEntity(
            id = subTask.id,
            taskId = taskId,
            description = subTask.description,
            isCompleted = isCompleted,
            cost = subTask.cost
        )
        taskDao.insertSubTasks(listOf(subTaskEntity))
    }

    override suspend fun addSubTask(taskId: Long, description: String, cost: Int) {
        val newSubTaskEntity = SubTaskEntity(
            taskId = taskId,
            description = description,
            isCompleted = false,
            cost = cost
        )
        taskDao.insertSubTasks(listOf(newSubTaskEntity))
    }
}