package szysz3.planty.domain.repository

import szysz3.planty.domain.model.Plant

interface PlantRepository {
    suspend fun searchPlants(query: String?, limit: Int, offset: Int): List<Plant>
    suspend fun getPlantById(id: Int): Plant?
}