package szysz3.planty.domain.usecase.task

import szysz3.planty.domain.model.Task
import szysz3.planty.domain.repository.TaskRepository
import szysz3.planty.domain.usecase.base.BaseUseCase
import javax.inject.Inject

class SaveTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) : BaseUseCase<Task, Unit>() {

    override suspend fun invoke(input: Task) {
        repository.saveTask(input)
    }
}
