package szysz3.planty.data.repository

import szysz3.planty.domain.model.SampleData
import szysz3.planty.domain.repository.SampleRepository
import javax.inject.Inject

class SampleRepositoryImpl @Inject constructor() : SampleRepository {
    override fun getSampleData(): SampleData {
        return SampleData("This is repository data!")
    }
}