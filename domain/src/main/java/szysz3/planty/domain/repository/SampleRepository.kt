package szysz3.planty.domain.repository

import szysz3.planty.domain.model.SampleData

interface SampleRepository {
    fun getSampleData(): SampleData
}