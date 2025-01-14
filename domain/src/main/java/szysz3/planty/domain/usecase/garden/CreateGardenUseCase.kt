package szysz3.planty.domain.usecase.garden

import szysz3.planty.domain.repository.GardenRepository
import szysz3.planty.domain.usecase.base.BaseUseCase
import javax.inject.Inject

class CreateGardenUseCase @Inject constructor(
    private val repository: GardenRepository
) : BaseUseCase<CreateGardenParams, Int>() {
    override suspend fun invoke(input: CreateGardenParams): Int {
        return repository.createGarden(
            name = input.name,
            rows = input.rows,
            columns = input.columns,
            parentGardenId = input.parentGardenId
        )
    }
}

data class CreateGardenParams(
    val name: String,
    val rows: Int,
    val columns: Int,
    val parentGardenId: Int?
)
