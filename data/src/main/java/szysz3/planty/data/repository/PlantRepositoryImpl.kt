package szysz3.planty.data.repository

import szysz3.planty.data.database.dao.PlantDao
import szysz3.planty.data.database.entity.toDomain
import szysz3.planty.domain.model.Plant
import szysz3.planty.domain.repository.PlantRepository
import javax.inject.Inject

class PlantRepositoryImpl @Inject constructor(
    private val plantDao: PlantDao
) : PlantRepository {
    override suspend fun insertPlant(plant: Plant) {
        TODO("Not yet implemented")
    }

    override suspend fun insertPlants(plants: List<Plant>) {
        TODO("Not yet implemented")
    }

    override suspend fun searchPlants(query: String?, limit: Int, offset: Int): List<Plant> {
        return plantDao.searchPlants(query, limit, offset).toDomain()
    }

    override suspend fun getPlantById(id: Int): Plant? {
        TODO("Not yet implemented")
    }

    override suspend fun deletePlant(plant: Plant) {
        TODO("Not yet implemented")
    }
}