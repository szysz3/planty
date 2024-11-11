package szysz3.planty.domain.usecase

import szysz3.planty.domain.model.GardenCell
import szysz3.planty.domain.repository.GardenRepository
import szysz3.planty.domain.usecase.base.BaseUseCase
import javax.inject.Inject

class SaveGardenCellUseCase @Inject constructor(
    private val repository: GardenRepository
) : BaseUseCase<GardenCell, Unit>() {
    override suspend fun invoke(input: GardenCell) {
        return repository.saveCell(input)
    }
}