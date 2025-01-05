package szysz3.planty.domain.usecase.task

import szysz3.planty.domain.model.Task
import szysz3.planty.domain.usecase.base.BaseUseCase
import javax.inject.Inject

class UpdateTaskOrderUseCase @Inject constructor(
) : BaseUseCase<UpdateTaskOrderUseCaseParams, List<Task>>() {

    override suspend fun invoke(input: UpdateTaskOrderUseCaseParams): List<Task> {
        if (input.tasks.isEmpty()) return emptyList()

        return input.tasks
            .toMutableList()
            .apply {
                val task = removeAt(input.fromIndex)
                add(input.toIndex, task)
            }
            .mapIndexed { index, task ->
                task.copy(index = index)
            }
    }
}

data class UpdateTaskOrderUseCaseParams(
    val tasks: List<Task>,
    val fromIndex: Int,
    val toIndex: Int
)