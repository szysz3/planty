package szysz3.planty.domain.usecase

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import szysz3.planty.domain.repository.PlantIdRepository
import szysz3.planty.domain.usecase.base.BaseUseCase
import javax.inject.Inject

class UploadPlantImageUseCase @Inject constructor(
    private val repository: PlantIdRepository,
    @ApplicationContext private val context: Context
) : BaseUseCase<Uri, Uri?>() {
    override suspend fun invoke(input: Uri): Uri? {
        val imageData: ByteArray =
            context.contentResolver.openInputStream(input).use { inputStream ->
                inputStream?.readBytes()
                    ?: throw IllegalArgumentException("Failed to read image data from Uri")
            }

        return repository.uploadPlantImage(imageData)
    }
}