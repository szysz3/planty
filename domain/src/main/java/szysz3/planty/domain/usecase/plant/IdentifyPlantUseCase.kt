package szysz3.planty.domain.usecase.plant

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import szysz3.planty.domain.model.remote.PlantIdResponse
import szysz3.planty.domain.repository.PlantIdRepository
import szysz3.planty.domain.repository.PlantRepository
import szysz3.planty.domain.usecase.base.BaseUseCase
import javax.inject.Inject

/**
 * Use case for identifying plants from images using PlantID API and matching with local plant database.
 */
class IdentifyPlantUseCase @Inject constructor(
    private val idRepo: PlantIdRepository,
    private val plantRepository: PlantRepository,
    @ApplicationContext private val context: Context
) : BaseUseCase<IdentifyPlantsParams, Flow<Result<PlantIdResponse>>>() {

    override suspend fun invoke(input: IdentifyPlantsParams): Flow<Result<PlantIdResponse>> = flow {
        try {
            val imageData = context.contentResolver.openInputStream(input.imageUris.first())?.use {
                it.readBytes()
            } ?: throw IllegalArgumentException("Failed to read image data from Uri")

            idRepo.identifyPlant(imageData, input.apiKey)
                .onSuccess { response ->
                    emit(Result.success(matchWithLocalPlants(response)))
                }
                .onFailure { error ->
                    emit(Result.failure(error))
                }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    private suspend fun matchWithLocalPlants(response: PlantIdResponse): PlantIdResponse {
        val matchedResults = response.results.map { result ->
            val matchingPlant = plantRepository.searchPlants(
                query = result.species.scientificNameWithoutAuthor,
                limit = 1,
                offset = 0
            ).firstOrNull()
            result.copy(plant = matchingPlant)
        }
        return response.copy(results = matchedResults)
    }
}

/**
 * Parameters for plant identification.
 * @property apiKey The API key for PlantID service
 * @property imageUris List of image URIs to process
 */
data class IdentifyPlantsParams(
    val apiKey: String,
    val imageUris: List<Uri>
) {
    init {
        require(apiKey.isNotBlank()) { "API key cannot be blank" }
        require(imageUris.isNotEmpty()) { "At least one image URI is required" }
    }
}