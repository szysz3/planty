package szysz3.planty.domain.usecase.task

import szysz3.planty.domain.model.Task
import szysz3.planty.domain.repository.TaskRepository
import szysz3.planty.domain.usecase.base.BaseUseCase
import szysz3.planty.domain.usecase.base.NoParams
import javax.inject.Inject

class GetAllTasksUseCase @Inject constructor(
    private val repository: TaskRepository
) : BaseUseCase<NoParams, List<Task>>() {
    override suspend fun invoke(input: NoParams): List<Task> {
        return repository.getTasks()
    }
}