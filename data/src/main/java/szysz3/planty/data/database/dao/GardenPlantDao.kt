package szysz3.planty.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import szysz3.planty.data.database.entity.GardenPlantEntity

@Dao
interface GardenPlantDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlant(plant: GardenPlantEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlants(plants: List<GardenPlantEntity>)

    @Query("SELECT * FROM garden_plants WHERE id = :plantId")
    suspend fun getGardenPlantById(plantId: Long?): GardenPlantEntity?

    @Query("DELETE FROM garden_plants")
    suspend fun clearAllPlants()
}
