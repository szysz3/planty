package szysz3.planty.domain.usecase

import szysz3.planty.domain.model.Plant
import szysz3.planty.domain.repository.PlantRepository
import szysz3.planty.domain.usecase.base.BaseUseCase
import javax.inject.Inject

class PlantSearchUseCase @Inject constructor(
    private val repository: PlantRepository
) : BaseUseCase<String, List<Plant>>() {
    override suspend fun invoke(input: String): List<Plant> {
        return repository.searchPlants(input)
    }
}