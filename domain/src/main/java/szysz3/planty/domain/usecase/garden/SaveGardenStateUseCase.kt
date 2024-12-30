package szysz3.planty.domain.usecase.garden

import szysz3.planty.domain.model.GardenState
import szysz3.planty.domain.repository.GardenRepository
import szysz3.planty.domain.usecase.base.BaseUseCase
import javax.inject.Inject

class SaveGardenStateUseCase @Inject constructor(
    private val repository: GardenRepository
) : BaseUseCase<GardenState, Unit>() {
    override suspend fun invoke(input: GardenState) {
        return repository.saveGardenState(input)
    }
}