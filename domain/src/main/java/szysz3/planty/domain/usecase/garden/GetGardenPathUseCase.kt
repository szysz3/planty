package szysz3.planty.domain.usecase.garden

import szysz3.planty.domain.model.Garden
import szysz3.planty.domain.repository.GardenRepository
import szysz3.planty.domain.usecase.base.BaseUseCase
import javax.inject.Inject

class GetGardenPathUseCase @Inject constructor(
    private val repository: GardenRepository
) : BaseUseCase<GardenIdParam, List<Garden>>() {
    override suspend fun invoke(input: GardenIdParam): List<Garden> {
        return repository.getGardenPath(input.gardenId)
    }
}