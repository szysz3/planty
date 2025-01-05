package szysz3.planty.domain.util

import szysz3.planty.domain.model.SubTask

object TestSubTaskFactory {

    fun createSubTask(
        id: Long = 1,
        description: String = "SubTask $id",
        isCompleted: Boolean = false,
        cost: Float = 0f
    ): SubTask {
        return SubTask(
            id = id,
            description = description,
            isCompleted = isCompleted,
            cost = cost
        )
    }

    fun createSubTasks(
        startId: Long = 1,
        count: Int = 2,
        isCompleted: Boolean = false,
        cost: Float = 0f
    ): List<SubTask> {
        return (startId until startId + count).map { id ->
            createSubTask(
                id = id,
                description = "SubTask $id",
                isCompleted = isCompleted,
                cost = cost
            )
        }
    }
}