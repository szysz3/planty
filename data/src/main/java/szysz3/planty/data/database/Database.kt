package szysz3.planty.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import szysz3.planty.data.database.dao.GardenCellDao
import szysz3.planty.data.database.dao.GardenPlantDao
import szysz3.planty.data.database.dao.PlantDao
import szysz3.planty.data.database.dao.PlantImageDao
import szysz3.planty.data.database.dao.TaskDao
import szysz3.planty.data.database.entity.GardenCellEntity
import szysz3.planty.data.database.entity.GardenPlantEntity
import szysz3.planty.data.database.entity.PlantEntity
import szysz3.planty.data.database.entity.PlantImageEntity
import szysz3.planty.data.database.entity.SubTaskEntity
import szysz3.planty.data.database.entity.TaskEntity

@Database(
    entities = [
        GardenCellEntity::class,
        PlantEntity::class,
        GardenPlantEntity::class,
        PlantImageEntity::class,
        TaskEntity::class,
        SubTaskEntity::class,
    ],
    version = 4
)
abstract class GardenDatabase : RoomDatabase() {
    abstract fun gardenCellDao(): GardenCellDao

    abstract fun plantDao(): PlantDao

    abstract fun gardenPlantDao(): GardenPlantDao

    abstract fun plantImageDao(): PlantImageDao

    abstract fun taskDao(): TaskDao

    companion object {
        private const val GARDEN_DATABASE_NAME = "garden_db"

        @Volatile
        private var instance: GardenDatabase? = null

        fun getInstance(context: Context): GardenDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    GardenDatabase::class.java,
                    GARDEN_DATABASE_NAME
                )
                    .createFromAsset("$GARDEN_DATABASE_NAME.db")
                    .build().also { instance = it }
            }
        }
    }
}