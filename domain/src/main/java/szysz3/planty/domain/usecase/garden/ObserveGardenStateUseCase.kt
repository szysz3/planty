package szysz3.planty.domain.usecase.garden

import kotlinx.coroutines.flow.Flow
import szysz3.planty.domain.model.GardenState
import szysz3.planty.domain.repository.GardenRepository
import szysz3.planty.domain.usecase.base.BaseUseCase
import javax.inject.Inject

class ObserveGardenStateUseCase @Inject constructor(
    private val repository: GardenRepository
) : BaseUseCase<GardenIdParam, Flow<GardenState>>() {
    override suspend fun invoke(input: GardenIdParam): Flow<GardenState> {
        return repository.observeGardenState(input.gardenId)
    }
}