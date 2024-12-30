package szysz3.planty.domain.usecase.garden

import szysz3.planty.domain.model.GardenCell
import szysz3.planty.domain.model.GardenState
import szysz3.planty.domain.model.Plant
import szysz3.planty.domain.repository.GardenRepository
import szysz3.planty.domain.usecase.base.BaseUseCase
import javax.inject.Inject

class SaveGardenCellUseCase @Inject constructor(
    private val repository: GardenRepository
) : BaseUseCase<SaveGardenCellUseCaseParams, GardenState>() {
    override suspend fun invoke(input: SaveGardenCellUseCaseParams): GardenState {
        val gardenState = repository.loadGardenState()
        val gardenStateCells = gardenState.cells.toMutableList()

        gardenStateCells.removeAll { it.row == input.row && it.column == input.column }
        gardenStateCells.add(GardenCell(0, input.row, input.column, input.plant))

        val updatedGardenState = gardenState.copy(cells = gardenStateCells)
        repository.saveGardenState(updatedGardenState)
        return updatedGardenState
    }
}

data class SaveGardenCellUseCaseParams(val row: Int, val column: Int, val plant: Plant?)