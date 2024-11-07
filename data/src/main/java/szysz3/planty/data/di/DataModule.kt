package szysz3.planty.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import szysz3.planty.data.repository.SampleRepositoryImpl
import szysz3.planty.domain.repository.SampleRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    @Singleton
    abstract fun bindSampleRepository(impl: SampleRepositoryImpl): SampleRepository
}