package szysz3.planty.data.repository.mock

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import szysz3.planty.domain.model.Plant
import szysz3.planty.domain.repository.PlantRepository
import java.io.BufferedReader
import javax.inject.Inject

class PlantMockRepositoryImpl @Inject constructor(@ApplicationContext context: Context) :
    PlantRepository {

    private val plantData by lazy {
        val inputStream = context.assets.open("sample_plant_data.csv")
        inputStream.bufferedReader().use(BufferedReader::readLines)
    }

    override suspend fun insertPlant(plant: Plant) {
        TODO("Not yet implemented")
    }

    override suspend fun insertPlants(plants: List<Plant>) {
        TODO("Not yet implemented")
    }

    override suspend fun searchPlants(query: String): List<Plant> {
        return plantData.toPlant().filter { plant ->
            plant.commonName?.contains(query) == true || plant.latinName.contains(query)
        }
    }

    override suspend fun getPlantById(id: Int): Plant? {
        TODO("Not yet implemented")
    }

    override suspend fun deletePlant(plant: Plant) {
        TODO("Not yet implemented")
    }

    override suspend fun getPlantsFromRange(startRange: Int, endRange: Int): List<Plant> {
        return plantData.toPlant().subList(startRange, endRange)
    }
}