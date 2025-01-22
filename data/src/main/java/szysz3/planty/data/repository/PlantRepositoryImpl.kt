package szysz3.planty.data.repository

import szysz3.planty.data.database.dao.PlantDao
import szysz3.planty.data.database.dao.PlantImageDao
import szysz3.planty.data.database.entity.toDomain
import szysz3.planty.domain.model.Plant
import szysz3.planty.domain.repository.PlantRepository
import javax.inject.Inject

/**
 * Implementation of [PlantRepository] that manages plant data and associated images
 * using local database access objects.
 */
class PlantRepositoryImpl @Inject constructor(
    private val plantDao: PlantDao,
    private val plantImageDao: PlantImageDao
) : PlantRepository {

    /**
     * Searches for plants based on an optional query string with pagination support.
     *
     * @param query Optional search query to filter plants
     * @param limit Maximum number of results to return
     * @param offset Number of results to skip for pagination
     * @return List of [Plant] entities matching the search criteria
     */
    override suspend fun searchPlants(query: String?, limit: Int, offset: Int): List<Plant> =
        plantDao.searchPlants(query, limit, offset).map { plantEntity ->
            plantEntity.toDomain(getImageUrls(plantEntity.id))
        }

    /**
     * Retrieves a specific plant by its ID along with associated images.
     *
     * @param id The unique identifier of the plant
     * @return The [Plant] entity if found, null otherwise
     */
    override suspend fun getPlantById(id: Int): Plant? =
        plantDao.getPlantById(id)?.toDomain(getImageUrls(id))

    private suspend fun getImageUrls(plantId: Int): List<String> =
        plantImageDao.getImagesByPlantId(plantId)
            ?.mapNotNull { it.imageUrl }
            .orEmpty()
}