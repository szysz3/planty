package szysz3.planty.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import szysz3.planty.data.repository.GardenRepositoryImpl
import szysz3.planty.data.repository.LocalFileRepositoryImpl
import szysz3.planty.data.repository.PlantIdRepositoryImpl
import szysz3.planty.data.repository.PlantImageRepositoryImpl
import szysz3.planty.data.repository.PlantRepositoryImpl
import szysz3.planty.domain.repository.GardenRepository
import szysz3.planty.domain.repository.LocalFileRepository
import szysz3.planty.domain.repository.PlantIdRepository
import szysz3.planty.domain.repository.PlantImageRepository
import szysz3.planty.domain.repository.PlantRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindGardenRepository(impl: GardenRepositoryImpl): GardenRepository

    @Binds
    @Singleton
    abstract fun bindPlantRepository(impl: PlantRepositoryImpl): PlantRepository

    @Binds
    @Singleton
    abstract fun bindFileRepository(impl: LocalFileRepositoryImpl): LocalFileRepository

    @Binds
    @Singleton
    abstract fun bindIdPlantRepository(impl: PlantIdRepositoryImpl): PlantIdRepository

    @Binds
    @Singleton
    abstract fun bindPlantImageRepository(impl: PlantImageRepositoryImpl): PlantImageRepository
}