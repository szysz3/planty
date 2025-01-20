package szysz3.planty.domain.usecase.plant

import szysz3.planty.domain.model.Plant
import szysz3.planty.domain.repository.PlantRepository
import szysz3.planty.domain.usecase.base.BaseUseCase
import javax.inject.Inject

class PlantSearchUseCase @Inject constructor(
    private val repository: PlantRepository
) : BaseUseCase<PlantSearchUseCaseParams, List<Plant>>() {
    override suspend fun invoke(input: PlantSearchUseCaseParams): List<Plant> {
        return repository.searchPlants(input.query, input.limit, input.offset)
    }
}

data class PlantSearchUseCaseParams(val query: String?, val limit: Int, val offset: Int)