package szysz3.planty.data.repository

import szysz3.planty.data.database.dao.PlantDao
import szysz3.planty.data.database.dao.PlantImageDao
import szysz3.planty.data.database.entity.toDomain
import szysz3.planty.domain.model.Plant
import szysz3.planty.domain.repository.PlantRepository
import javax.inject.Inject

class PlantRepositoryImpl @Inject constructor(
    private val plantDao: PlantDao,
    private val plantImageDao: PlantImageDao
) : PlantRepository {

    override suspend fun searchPlants(query: String?, limit: Int, offset: Int): List<Plant> =
        plantDao.searchPlants(query, limit, offset).map { plantEntity ->
            plantEntity.toDomain(getImageUrls(plantEntity.id))
        }

    override suspend fun getPlantById(id: Int): Plant? =
        plantDao.getPlantById(id)?.toDomain(getImageUrls(id))

    private suspend fun getImageUrls(plantId: Int): List<String> =
        plantImageDao.getImagesByPlantId(plantId)
            ?.mapNotNull { it.imageUrl }
            .orEmpty()
}