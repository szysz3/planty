package szysz3.planty.data.repository.mock

import android.content.Context
import com.opencsv.CSVReader
import dagger.hilt.android.qualifiers.ApplicationContext
import szysz3.planty.domain.model.Plant
import szysz3.planty.domain.repository.PlantRepository
import java.io.InputStreamReader
import javax.inject.Inject

class PlantMockRepositoryImpl @Inject constructor(@ApplicationContext context: Context) :
    PlantRepository {

    private val plantData by lazy {
        val plants = mutableListOf<Plant>()
        val inputStream = context.assets.open("sample_plant_data.csv")

        CSVReader(InputStreamReader(inputStream)).use { csvReader ->
            var line: Array<String>?
            csvReader.readNext() // Skip header line if present
            while (csvReader.readNext().also { line = it } != null) {
                line?.let { plants.add(it.toPlant()) }
            }
        }
        plants
    }

    override suspend fun insertPlant(plant: Plant) {
        TODO("Not yet implemented")
    }

    override suspend fun insertPlants(plants: List<Plant>) {
        TODO("Not yet implemented")
    }

    override suspend fun searchPlants(query: String?, limit: Int, offset: Int): List<Plant> {
        return if (query.isNullOrBlank()) {
            plantData.subList(limit, offset)
        } else {
            plantData.filter { plant ->
                plant.commonName?.lowercase()
                    ?.contains(query) == true || plant.latinName.lowercase()
                    .contains(query)
            }.subList(limit, offset)
        }
    }

    override suspend fun getPlantById(id: Int): Plant? {
        TODO("Not yet implemented")
    }

    override suspend fun deletePlant(plant: Plant) {
        TODO("Not yet implemented")
    }
}