package szysz3.planty.domain.usecase.garden

import szysz3.planty.domain.model.Plant
import szysz3.planty.domain.repository.GardenRepository
import szysz3.planty.domain.usecase.base.BaseUseCase
import javax.inject.Inject

class SaveGardenCellUseCase @Inject constructor(
    private val repository: GardenRepository
) : BaseUseCase<SaveGardenCellUseCaseParams, Unit>() {
    override suspend fun invoke(input: SaveGardenCellUseCaseParams) {
        repository.updateGardenCell(
            row = input.row,
            column = input.column,
            plant = input.plant,
            gardenId = input.gardenId
        )
    }
}

data class SaveGardenCellUseCaseParams(
    val row: Int,
    val column: Int,
    val plant: Plant?,
    val gardenId: Int
)