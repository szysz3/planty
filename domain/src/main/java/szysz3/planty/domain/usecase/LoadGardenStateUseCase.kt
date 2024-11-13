package szysz3.planty.domain.usecase

import szysz3.planty.domain.model.GardenState
import szysz3.planty.domain.repository.GardenRepository
import szysz3.planty.domain.usecase.base.BaseUseCase
import szysz3.planty.domain.usecase.base.NoParams
import javax.inject.Inject

class LoadGardenStateUseCase @Inject constructor(
    private val repository: GardenRepository
) : BaseUseCase<NoParams, GardenState>() {
    override suspend fun invoke(input: NoParams): GardenState {
        return repository.loadGardenState()
    }
}