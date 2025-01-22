package szysz3.planty.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import szysz3.planty.data.database.entity.GardenEntity

@Dao
interface GardenDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertGarden(garden: GardenEntity): Long

    @Update
    suspend fun updateGarden(garden: GardenEntity)

    @Query("SELECT * FROM gardens WHERE id = :gardenId")
    suspend fun getGardenById(gardenId: Int): GardenEntity?

    @Query("SELECT * FROM gardens WHERE parentGardenId IS NULL")
    suspend fun getRootGarden(): GardenEntity?

    @Query("DELETE FROM gardens WHERE id = :gardenId")
    suspend fun deleteGarden(gardenId: Int)
}