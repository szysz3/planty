package szysz3.planty.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import szysz3.planty.data.database.entity.SubTaskEntity
import szysz3.planty.data.database.entity.TaskEntity
import szysz3.planty.data.database.entity.TaskWithSubTasks

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubTasks(subTasks: List<SubTaskEntity>)

    @Transaction
    @Query("SELECT * FROM tasks")
    suspend fun getTasksWithSubTasks(): List<TaskWithSubTasks>

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Query("DELETE FROM subtasks WHERE taskId = :taskId")
    suspend fun deleteSubTasksByTaskId(taskId: Long)
}