package szysz3.planty.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import szysz3.planty.data.database.entity.GardenPlantEntity

@Dao
interface GardenPlantDao {

    // Insert a single plant
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlant(plant: GardenPlantEntity): Long

    // Insert multiple plants
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlants(plants: List<GardenPlantEntity>)

    // Fetch a plant by its ID
    @Query("SELECT * FROM garden_plants WHERE id = :plantId")
    suspend fun getGardenPlantById(plantId: Long?): GardenPlantEntity?

    // Delete all plants
    @Query("DELETE FROM garden_plants")
    suspend fun clearAllPlants()
}
