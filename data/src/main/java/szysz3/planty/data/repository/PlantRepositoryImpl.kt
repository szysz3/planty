package szysz3.planty.data.repository

import szysz3.planty.data.database.dao.PlantDao
import szysz3.planty.data.database.dao.PlantImageDao
import szysz3.planty.data.database.entity.toDomain
import szysz3.planty.domain.model.Plant
import szysz3.planty.domain.repository.PlantRepository
import javax.inject.Inject

class PlantRepositoryImpl @Inject constructor(
    private val plantDao: PlantDao,
    private val plantImagesDao: PlantImageDao
) : PlantRepository {

    override suspend fun searchPlants(query: String?, limit: Int, offset: Int): List<Plant> {
        return plantDao.searchPlants(query, limit, offset).map { plantEntity ->
            val images =
                plantImagesDao.getImagesByPlantId(plantEntity.id)?.map { it.imageUrl }.orEmpty()
            plantEntity.toDomain(images)
        }
    }

    override suspend fun getPlantById(id: Int): Plant? {
        val plant = plantDao.getPlantById(id)
        plant?.let {
            val images = plantImagesDao.getImagesByPlantId(plant.id)?.map { it.imageUrl }
            return plant.toDomain(images)
        }

        return null
    }
}