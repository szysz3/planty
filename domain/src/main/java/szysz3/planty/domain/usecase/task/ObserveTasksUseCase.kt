package szysz3.planty.domain.usecase.task

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import szysz3.planty.domain.model.Task
import szysz3.planty.domain.repository.TaskRepository
import szysz3.planty.domain.usecase.base.BaseUseCase
import szysz3.planty.domain.usecase.base.NoParams
import javax.inject.Inject

class ObserveTasksUseCase @Inject constructor(
    private val repository: TaskRepository
) : BaseUseCase<NoParams, Flow<List<Task>>>() {
    override suspend fun invoke(input: NoParams): Flow<List<Task>> {
        return repository.getTasksWithSubTasksFlow().map { tasks ->
            tasks.sortedWith(
                compareBy<Task> { it.isCompleted }
                    .thenBy { it.index }
            )
        }
    }
}