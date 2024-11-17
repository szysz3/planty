package szysz3.planty.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import szysz3.planty.data.database.dao.GardenCellDao
import szysz3.planty.data.database.dao.PlantDao
import szysz3.planty.data.database.entity.GardenCellEntity
import szysz3.planty.data.database.entity.PlantEntity

@Database(entities = [GardenCellEntity::class, PlantEntity::class], version = 2)
abstract class GardenDatabase : RoomDatabase() {
    abstract fun gardenCellDao(): GardenCellDao

    abstract fun plantDao(): PlantDao

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
                ).fallbackToDestructiveMigration() // Replace with migrations in production
                    .build().also { instance = it }
            }
        }
    }
}