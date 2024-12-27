package szysz3.planty.domain.usecase.task

import kotlinx.coroutines.flow.Flow
import szysz3.planty.domain.model.Task
import szysz3.planty.domain.repository.TaskRepository
import szysz3.planty.domain.usecase.base.BaseUseCase
import szysz3.planty.domain.usecase.base.NoParams
import javax.inject.Inject

class ObserveTasksUseCase @Inject constructor(
    private val repository: TaskRepository
) : BaseUseCase<NoParams, Flow<List<Task>>>() {
    override suspend fun invoke(input: NoParams): Flow<List<Task>> {
        return repository.tasksFlow
    }
}