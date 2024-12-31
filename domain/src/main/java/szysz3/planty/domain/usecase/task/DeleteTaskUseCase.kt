package szysz3.planty.domain.usecase.task

import szysz3.planty.domain.model.Task
import szysz3.planty.domain.repository.TaskRepository
import szysz3.planty.domain.usecase.base.BaseUseCase
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) : BaseUseCase<Task, Unit>() {
    override suspend fun invoke(input: Task) {
        val tasks = repository.getTasks()
            .toMutableList()
            .apply {
                remove(input)
            }
            .mapIndexed { index, task ->
                task.copy(index = index)
            }

        repository.deleteTask(input)
        repository.saveTasks(tasks)
    }
}