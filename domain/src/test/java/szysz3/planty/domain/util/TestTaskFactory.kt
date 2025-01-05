package szysz3.planty.domain.util

import szysz3.planty.domain.model.SubTask
import szysz3.planty.domain.model.Task

object TestTaskFactory {

    fun createTask(
        id: Long = 0,
        title: String = "Task $id",
        subTasks: List<SubTask> = emptyList(),
        isCompleted: Boolean = false,
        color: Int = 0xFFFFFF,
        index: Int = id.toInt() - 1
    ): Task {
        return Task(
            id = id,
            title = title,
            subTasks = subTasks,
            isCompleted = isCompleted,
            color = color,
            index = index
        )
    }

    fun createTasks(
        startId: Long = 1,
        count: Int = 3,
        subTaskCount: Int = 2,
        isCompleted: Boolean = false,
        color: Int = 0xFFFFFF
    ): List<Task> {
        return (startId until startId + count).map { id ->
            createTask(
                id = id,
                subTasks = TestSubTaskFactory.createSubTasks(
                    startId = id,
                    count = subTaskCount,
                    isCompleted = isCompleted
                ),
                color = color,
                index = (id - 1).toInt()
            )
        }
    }
}
