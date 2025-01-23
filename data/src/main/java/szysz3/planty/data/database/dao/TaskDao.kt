package szysz3.planty.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import szysz3.planty.data.database.entity.SubTaskEntity
import szysz3.planty.data.database.entity.TaskEntity
import szysz3.planty.data.database.entity.TaskWithSubTasks

@Dao
interface TaskDao {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity): Long

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<TaskEntity>): List<Long>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubTasks(subTasks: List<SubTaskEntity>)

    @Transaction
    @Query("SELECT * FROM tasks")
    suspend fun getTasksWithSubTasks(): List<TaskWithSubTasks>

    @Transaction
    @Query("SELECT * FROM tasks")
    fun getTasksWithSubTasksFlow(): Flow<List<TaskWithSubTasks>>

    @Transaction
    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTasksWithSubTasks(taskId: Long): TaskWithSubTasks?

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Transaction
    @Query("DELETE FROM subtasks WHERE taskId = :taskId")
    suspend fun deleteSubTasksByTaskId(taskId: Long)

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Update
    suspend fun updateTasks(tasks: List<TaskEntity>)

    @Transaction
    @Update
    suspend fun updateSubTasks(subTasks: List<SubTaskEntity>)
}