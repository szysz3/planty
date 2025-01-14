package szysz3.planty.domain.usecase.garden

import szysz3.planty.domain.repository.GardenRepository
import szysz3.planty.domain.usecase.base.BaseUseCase
import javax.inject.Inject

class ClearGardenUseCase @Inject constructor(
    private val repository: GardenRepository
) : BaseUseCase<GardenIdParam, Unit>() {
    override suspend fun invoke(input: GardenIdParam) {
        return repository.clearGardenState(input.gardenId)
    }
}