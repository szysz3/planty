package szysz3.planty.domain.usecase.task

import szysz3.planty.domain.model.Task
import szysz3.planty.domain.repository.TaskRepository
import szysz3.planty.domain.usecase.base.BaseUseCase
import javax.inject.Inject

class GetTaskByIdUseCase @Inject constructor(
    private val repository: TaskRepository
) : BaseUseCase<Int, Task?>() {
    override suspend fun invoke(input: Int): Task? {
        return repository.getTaskById(input.toLong())
    }
}