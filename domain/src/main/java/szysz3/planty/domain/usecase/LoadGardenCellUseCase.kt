package szysz3.planty.domain.usecase

import szysz3.planty.domain.model.GardenCell
import szysz3.planty.domain.repository.GardenRepository
import szysz3.planty.domain.usecase.base.BaseUseCase
import szysz3.planty.domain.usecase.base.NoParams
import javax.inject.Inject

class LoadGardenCellUseCase @Inject constructor(
    private val repository: GardenRepository
) : BaseUseCase<NoParams, List<GardenCell>>() {
    override suspend fun invoke(input: NoParams): List<GardenCell> {
        return repository.loadGarden()
    }
}