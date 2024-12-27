package szysz3.planty.data.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class TaskWithSubTasks(
    @Embedded val task: TaskEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "taskId"
    )
    val subTasks: List<SubTaskEntity>
)