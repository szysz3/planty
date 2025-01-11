package szysz3.planty.domain.usecase.garden

import szysz3.planty.domain.repository.GardenRepository
import szysz3.planty.domain.usecase.base.BaseUseCase
import javax.inject.Inject

class MergeCellsUseCase @Inject constructor(
    private val repository: GardenRepository
) : BaseUseCase<MergeCellsParams, Long>() {
    override suspend fun invoke(input: MergeCellsParams): Long {
        require(input.selectedCells.isNotEmpty()) { "No cells selected for merging" }

        val minRow = input.selectedCells.minOf { it.first }
        val maxRow = input.selectedCells.maxOf { it.first }
        val minCol = input.selectedCells.minOf { it.second }
        val maxCol = input.selectedCells.maxOf { it.second }

        // Verify selection forms a rectangle
        val expectedCells = (maxRow - minRow + 1) * (maxCol - minCol + 1)
        require(input.selectedCells.size == expectedCells) {
            "Selected cells must form a rectangle"
        }

        return repository.mergeCells(
            parentGardenId = input.parentGardenId,
            startRow = minRow,
            startColumn = minCol,
            endRow = maxRow,
            endColumn = maxCol
        )
    }
}

data class MergeCellsParams(
    val parentGardenId: Int?,
    val selectedCells: Set<Pair<Int, Int>>
)