package szysz3.planty.domain.usecase

import szysz3.planty.domain.model.SampleData
import szysz3.planty.domain.repository.SampleRepository
import szysz3.planty.domain.usecase.base.BaseUseCase
import szysz3.planty.domain.usecase.base.NoParams
import javax.inject.Inject

class SampleUseCase @Inject constructor(private val repository: SampleRepository) :
    BaseUseCase<NoParams, SampleData>() {

    // TODO: do not use data model directly
    override fun execute(input: NoParams): SampleData {
        return repository.getSampleData()
    }
}