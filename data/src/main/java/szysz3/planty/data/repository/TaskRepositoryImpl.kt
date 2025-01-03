package szysz3.planty.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import szysz3.planty.data.database.dao.TaskDao
import szysz3.planty.data.database.entity.SubTaskEntity
import szysz3.planty.data.database.entity.toDomain
import szysz3.planty.data.database.entity.toEntity
import szysz3.planty.domain.model.SubTask
import szysz3.planty.domain.model.Task
import szysz3.planty.domain.repository.TaskRepository
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(private val taskDao: TaskDao) : TaskRepository {

    override suspend fun getTasksWithSubTasksFlow(): Flow<List<Task>> =
        taskDao.getTasksWithSubTasksFlow()
            .map { taskWithSubTasksList ->
                taskWithSubTasksList.map { it.toDomain() }
            }

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

    override suspend fun saveTasks(tasks: List<Task>) {
        val taskEntities = tasks.map { it.toEntity().first }
        val taskIds = taskDao.insertTasks(taskEntities)

        val allSubTaskEntities = tasks.flatMapIndexed { index, task ->
            task.subTasks.map { subTask ->
                subTask.toEntity(taskIds[index])
            }
        }

        taskDao.insertSubTasks(allSubTaskEntities)
    }

    override suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task.toEntity().first)
    }

    override suspend fun updateSubTaskCompletion(
        taskId: Long,
        subTask: SubTask,
        isCompleted: Boolean
    ) {
        taskDao.insertSubTasks(listOf(subTask.toEntity(taskId, isCompleted)))
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

    override suspend fun updateTask(task: Task) {
        val taskEntity = task.toEntity().first
        taskDao.updateTask(taskEntity)

        val subTaskEntities = task.subTasks.map { it.toEntity(task.id) }
        taskDao.updateSubTasks(subTaskEntities)
    }

    override suspend fun updateTasks(tasks: List<Task>) {
        val taskEntities = tasks.map { it.toEntity().first }
        taskDao.updateTasks(taskEntities)

        val allSubTaskEntities = tasks.flatMap { task ->
            task.subTasks.map { subTask ->
                subTask.toEntity(task.id)
            }
        }
        taskDao.updateSubTasks(allSubTaskEntities)
    }
}