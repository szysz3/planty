package szysz3.planty.domain.usecase.garden

import szysz3.planty.domain.model.GardenState
import szysz3.planty.domain.repository.GardenRepository
import szysz3.planty.domain.usecase.base.BaseUseCase
import javax.inject.Inject

class LoadGardenStateUseCase @Inject constructor(
    private val repository: GardenRepository
) : BaseUseCase<GardenIdParam, GardenState>() {
    override suspend fun invoke(input: GardenIdParam): GardenState {
        return repository.loadGardenState(input.gardenId)
    }
}