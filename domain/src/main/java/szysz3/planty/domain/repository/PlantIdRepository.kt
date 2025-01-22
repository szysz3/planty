package szysz3.planty.domain.repository

import szysz3.planty.domain.model.remote.PlantIdResponse

interface PlantIdRepository {
    suspend fun identifyPlant(image: ByteArray, apiKey: String): Result<PlantIdResponse>
}