package szysz3.planty.domain.usecase.task

import szysz3.planty.domain.model.Task
import szysz3.planty.domain.repository.TaskRepository
import szysz3.planty.domain.usecase.base.BaseUseCase
import javax.inject.Inject

class AddTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) : BaseUseCase<Task, Unit>() {

    override suspend fun invoke(input: Task) {
        val existingTasks = repository.getTasks()
        val shiftedExistingTasks = existingTasks.map { it.copy(index = it.index + 1) }
        val newTask = input.copy(index = 0)

        repository.saveTasks(shiftedExistingTasks + newTask)
    }
}
