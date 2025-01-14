package szysz3.planty.domain.usecase.garden

import szysz3.planty.domain.repository.GardenRepository
import szysz3.planty.domain.usecase.base.BaseUseCase
import javax.inject.Inject

class CreateSubGardenForMergedCellUseCase @Inject constructor(
    private val repository: GardenRepository,
) : BaseUseCase<CreateSubGardenForMergedCellParams, Int>() {
    override suspend fun invoke(input: CreateSubGardenForMergedCellParams): Int {
        // Create new garden
        val subGardenId = repository.createGarden(
            name = input.name,
            rows = input.rows,
            columns = input.columns,
            parentGardenId = input.mergedCellId
        )

        // Link it to merged cell
        repository.linkSubGardenToMergedCell(
            mergedCellId = input.mergedCellId,
            subGardenId = subGardenId
        )

        return subGardenId
    }
}

data class CreateSubGardenForMergedCellParams(
    val name: String,
    val rows: Int,
    val columns: Int,
    val mergedCellId: Int
)