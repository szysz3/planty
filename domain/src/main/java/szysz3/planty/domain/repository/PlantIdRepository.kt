package szysz3.planty.domain.repository

import android.net.Uri

interface PlantIdRepository {
    suspend fun uploadPlantImage(imageData: ByteArray): Uri?
}