package szysz3.planty.domain.usecase

import szysz3.planty.domain.model.Plant
import szysz3.planty.domain.repository.PlantRepository
import szysz3.planty.domain.usecase.base.BaseUseCase
import javax.inject.Inject

class PlantSearchUseCase @Inject constructor(
    private val repository: PlantRepository
) : BaseUseCase<PlantSearchUseCaseParams, List<Plant>>() {
    override suspend fun invoke(input: PlantSearchUseCaseParams): List<Plant> {
        return repository.searchPlants(input.query, input.startRange, input.endRange)
    }
}

data class PlantSearchUseCaseParams(val query: String?, val startRange: Int, val endRange: Int)