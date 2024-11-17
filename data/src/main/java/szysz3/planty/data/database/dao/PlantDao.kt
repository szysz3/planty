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

    @Query("SELECT * FROM plants WHERE latinName LIKE :query OR commonName LIKE :query")
    suspend fun searchPlants(query: String): List<PlantEntity>

    @Query("SELECT * FROM plants LIMIT :endIndex OFFSET :startIndex")
    suspend fun getPlantsByRange(startIndex: Int, endIndex: Int): List<PlantEntity>

    @Query("SELECT * FROM plants WHERE id = :id")
    suspend fun getPlantById(id: Int): PlantEntity?

    @Delete
    suspend fun deletePlant(plant: PlantEntity)
}