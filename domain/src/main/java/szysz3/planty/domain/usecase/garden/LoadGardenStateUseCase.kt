package szysz3.planty.domain.usecase.garden

import szysz3.planty.domain.model.GardenState
import szysz3.planty.domain.repository.GardenRepository
import szysz3.planty.domain.repository.PlantRepository
import szysz3.planty.domain.usecase.base.BaseUseCase
import javax.inject.Inject

class LoadGardenStateUseCase @Inject constructor(
    private val gardenRepository: GardenRepository,
    private val plantRepository: PlantRepository
) : BaseUseCase<GardenIdParam, GardenState>() {
    override suspend fun invoke(input: GardenIdParam): GardenState {
        val gardenState = gardenRepository.loadGardenState(input.gardenId)
        return gardenState.copy(
            cells = gardenState.cells.map { cell ->
                if (cell.plant != null) {
                    val plantWithImages = plantRepository.getPlantById(cell.plant.id)
                    cell.copy(plant = plantWithImages)
                } else {
                    cell
                }
            },
            mergedCells = gardenState.mergedCells.map { mergedCell ->
                if (mergedCell.subGardenId != null) {
                    val subGarden = gardenRepository.getGarden(mergedCell.subGardenId)
                    mergedCell.copy(
                        subGardenName = subGarden?.name,
                        subGardenRows = subGarden?.rows,
                        subGardenColumns = subGarden?.columns
                    )
                } else {
                    mergedCell
                }
            }
        )
    }
}