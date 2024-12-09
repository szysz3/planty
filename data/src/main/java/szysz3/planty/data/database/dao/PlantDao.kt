package szysz3.planty.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import szysz3.planty.data.database.entity.PlantEntity

@Dao
interface PlantDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlant(plant: PlantEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlants(plants: List<PlantEntity>)

    @Query(
        """
        SELECT * FROM plants 
        WHERE (:query IS NULL OR latinName LIKE '%' || :query || '%' OR commonName LIKE '%' || :query || '%')
        LIMIT :limit OFFSET :offset
        """
    )
    suspend fun searchPlants(query: String?, limit: Int, offset: Int): List<PlantEntity>

    @Query("SELECT * FROM plants WHERE id = :id")
    suspend fun getPlantById(id: Int): PlantEntity?

    @Delete
    suspend fun deletePlant(plant: PlantEntity)
}