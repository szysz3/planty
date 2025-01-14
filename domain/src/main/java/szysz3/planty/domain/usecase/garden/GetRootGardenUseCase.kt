package szysz3.planty.domain.usecase.garden

import szysz3.planty.domain.model.Garden
import szysz3.planty.domain.repository.GardenRepository
import szysz3.planty.domain.usecase.base.BaseUseCase
import szysz3.planty.domain.usecase.base.NoParams
import javax.inject.Inject

class GetRootGardenUseCase @Inject constructor(
    private val repository: GardenRepository
) : BaseUseCase<NoParams, Garden?>() {
    override suspend fun invoke(input: NoParams): Garden? {
        return repository.getRootGarden()
    }
}