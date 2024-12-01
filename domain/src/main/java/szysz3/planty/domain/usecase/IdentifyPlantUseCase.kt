package szysz3.planty.domain.usecase

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import szysz3.planty.domain.model.remote.PlantIdResponse
import szysz3.planty.domain.repository.CloudFileRepository
import szysz3.planty.domain.repository.PlantIdRepository
import szysz3.planty.domain.usecase.base.BaseUseCase
import javax.inject.Inject

class IdentifyPlantUseCase @Inject constructor(
    private val cloudFileRepo: CloudFileRepository,
    private val idRepo: PlantIdRepository,
    @ApplicationContext private val context: Context
) : BaseUseCase<IdentifyPlantsParams, PlantIdResponse?>() {
    override suspend fun invoke(input: IdentifyPlantsParams): PlantIdResponse? {
        val imageData: ByteArray =
            context.contentResolver.openInputStream(input.imageUris.first()).use { inputStream ->
                inputStream?.readBytes()
                    ?: throw IllegalArgumentException("Failed to read image data from Uri")
            }

        val plantImage = cloudFileRepo.uploadPlantImage(imageData)
        plantImage?.let {
            return idRepo.identifyPlant(
                imageUrls = listOf(it.toString()),
                apiKey = input.apiKey
            )
        }

        return null
    }
}

data class IdentifyPlantsParams(val apiKey: String, val imageUris: List<Uri>)