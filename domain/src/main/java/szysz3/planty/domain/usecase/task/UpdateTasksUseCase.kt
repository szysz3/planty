package szysz3.planty.domain.usecase.task

import szysz3.planty.domain.model.Task
import szysz3.planty.domain.repository.TaskRepository
import szysz3.planty.domain.usecase.base.BaseUseCase
import javax.inject.Inject

class UpdateTasksUseCase @Inject constructor(
    private val repository: TaskRepository
) : BaseUseCase<List<Task>, Unit>() {
    override suspend fun invoke(input: List<Task>) {
        return repository.saveTasks(input)
    }
}