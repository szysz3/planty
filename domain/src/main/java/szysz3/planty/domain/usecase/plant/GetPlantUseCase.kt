package szysz3.planty.domain.usecase.plant

import szysz3.planty.domain.model.Plant
import szysz3.planty.domain.repository.PlantRepository
import szysz3.planty.domain.usecase.base.BaseUseCase
import javax.inject.Inject

class GetPlantUseCase @Inject constructor(
    private val repository: PlantRepository
) : BaseUseCase<Int, Plant?>() {
    override suspend fun invoke(input: Int): Plant? {
        return repository.getPlantById(input)
    }
}