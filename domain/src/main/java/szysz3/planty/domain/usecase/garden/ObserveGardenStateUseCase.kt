package szysz3.planty.domain.usecase.garden

import kotlinx.coroutines.flow.Flow
import szysz3.planty.domain.model.GardenState
import szysz3.planty.domain.repository.GardenRepository
import szysz3.planty.domain.usecase.base.BaseUseCase
import szysz3.planty.domain.usecase.base.NoParams
import javax.inject.Inject

class ObserveGardenStateUseCase @Inject constructor(
    private val repository: GardenRepository
) : BaseUseCase<NoParams, Flow<GardenState>>() {
    override suspend fun invoke(input: NoParams): Flow<GardenState> {
        return repository.observeGardenState()
    }
}