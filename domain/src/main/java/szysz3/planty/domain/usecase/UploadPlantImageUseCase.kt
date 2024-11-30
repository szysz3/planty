package szysz3.planty.domain.usecase

import android.graphics.Bitmap
import android.net.Uri
import szysz3.planty.domain.repository.PlantIdRepository
import szysz3.planty.domain.usecase.base.BaseUseCase
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class UploadPlantImageUseCase @Inject constructor(
    private val repository: PlantIdRepository
) : BaseUseCase<Bitmap, Uri?>() {
    override suspend fun invoke(input: Bitmap): Uri? {
        val baos = ByteArrayOutputStream()
        input.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageData = baos.toByteArray()

        return repository.uploadPlantImage(imageData)
    }
}