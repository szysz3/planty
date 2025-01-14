package szysz3.planty.domain.usecase.garden

import szysz3.planty.domain.model.CellRange
import szysz3.planty.domain.repository.GardenRepository
import szysz3.planty.domain.usecase.base.BaseUseCase
import javax.inject.Inject

class CreateMergedCellUseCase @Inject constructor(
    private val repository: GardenRepository
) : BaseUseCase<CreateMergedCellParams, Int>() {
    override suspend fun invoke(input: CreateMergedCellParams): Int {
        return repository.createMergedCell(input.gardenId, input.cellRange)
    }
}

data class CreateMergedCellParams(
    val gardenId: Int,
    val cellRange: CellRange
)
