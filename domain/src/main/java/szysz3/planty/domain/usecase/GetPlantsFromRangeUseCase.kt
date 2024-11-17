package szysz3.planty.domain.usecase

import szysz3.planty.domain.model.Plant
import szysz3.planty.domain.repository.PlantRepository
import szysz3.planty.domain.usecase.base.BaseUseCase
import javax.inject.Inject

class GetPlantsFromRangeUseCase @Inject constructor(
    private val repository: PlantRepository
) : BaseUseCase<GetPlantsFromRangeParams, List<Plant>>() {
    override suspend fun invoke(input: GetPlantsFromRangeParams): List<Plant> {
        return repository.getPlantsFromRange(input.startRange, input.endRange)
    }
}

data class GetPlantsFromRangeParams(val startRange: Int, val endRange: Int)