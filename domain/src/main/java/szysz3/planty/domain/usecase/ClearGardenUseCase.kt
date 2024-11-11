package szysz3.planty.domain.usecase

import szysz3.planty.domain.repository.GardenRepository
import szysz3.planty.domain.usecase.base.BaseUseCase
import szysz3.planty.domain.usecase.base.NoParams
import javax.inject.Inject

class ClearGardenUseCase @Inject constructor(
    private val repository: GardenRepository
) : BaseUseCase<NoParams, Unit>() {
    override suspend fun invoke(input: NoParams) {
        return repository.clearGarden()
    }
}