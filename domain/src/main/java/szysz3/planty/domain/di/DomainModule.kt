package szysz3.planty.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import szysz3.planty.domain.repository.SampleRepository
import szysz3.planty.domain.usecase.SampleUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DomainModule {
    @Provides
    @Singleton
    fun provideSampleUseCase(repository: SampleRepository): SampleUseCase {
        return SampleUseCase(repository)
    }
}