package szysz3.planty.domain.repository

import szysz3.planty.domain.model.remote.PlantIdResponse

interface PlantIdRepository {
    suspend fun identifyPlant(
        imageUrls: List<String>,
        apiKey: String
    ): PlantIdResponse
}