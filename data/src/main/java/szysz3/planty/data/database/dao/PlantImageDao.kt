package szysz3.planty.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import szysz3.planty.data.database.entity.PlantImageEntity

@Dao
interface PlantImageDao {

    @Query("SELECT * FROM plant_images WHERE plantId = :plantId")
    suspend fun getImagesByPlantId(plantId: Int): List<PlantImageEntity>?
}