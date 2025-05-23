package szysz3.planty.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import szysz3.planty.data.database.GardenDatabase
import szysz3.planty.data.database.dao.GardenCellDao
import szysz3.planty.data.database.dao.GardenDao
import szysz3.planty.data.database.dao.GardenPlantDao
import szysz3.planty.data.database.dao.MergedCellDao
import szysz3.planty.data.database.dao.PlantDao
import szysz3.planty.data.database.dao.PlantImageDao
import szysz3.planty.data.database.dao.TaskDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): GardenDatabase {
        return GardenDatabase.getInstance(appContext)
    }

    @Provides
    @Singleton
    fun provideGardenCellDao(database: GardenDatabase): GardenCellDao {
        return database.gardenCellDao()
    }

    @Provides
    @Singleton
    fun providePlantDao(database: GardenDatabase): PlantDao {
        return database.plantDao()
    }

    @Provides
    @Singleton
    fun provideGardenPlantDao(database: GardenDatabase): GardenPlantDao {
        return database.gardenPlantDao()
    }

    @Provides
    @Singleton
    fun providePlantImageDao(database: GardenDatabase): PlantImageDao {
        return database.plantImageDao()
    }

    @Provides
    @Singleton
    fun provideTaskDao(database: GardenDatabase): TaskDao {
        return database.taskDao()
    }

    @Provides
    @Singleton
    fun provideGardenDao(database: GardenDatabase): GardenDao {
        return database.gardenDao()
    }

    @Provides
    @Singleton
    fun provideMergedCellDao(database: GardenDatabase): MergedCellDao {
        return database.mergedCellDao()
    }
}