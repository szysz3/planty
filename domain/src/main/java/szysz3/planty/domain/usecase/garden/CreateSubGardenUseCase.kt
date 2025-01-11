package szysz3.planty.domain.usecase.garden

import szysz3.planty.domain.repository.GardenRepository
import szysz3.planty.domain.usecase.base.BaseUseCase
import javax.inject.Inject

class CreateSubGardenUseCase @Inject constructor(
    private val repository: GardenRepository
) : BaseUseCase<CreateSubGardenParams, Long>() {
    override suspend fun invoke(input: CreateSubGardenParams): Long {
        require(input.rows > 0) { "Rows must be positive" }
        require(input.columns > 0) { "Columns must be positive" }
        require(input.rows <= 20) { "Maximum 20 rows allowed" }
        require(input.columns <= 20) { "Maximum 20 columns allowed" }

        return repository.createSubGarden(
            mergedCellId = input.mergedCellId,
            rows = input.rows,
            columns = input.columns
        )
    }
}

data class CreateSubGardenParams(
    val mergedCellId: Int,
    val rows: Int,
    val columns: Int
)
