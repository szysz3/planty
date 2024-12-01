package szysz3.planty.domain.repository

import android.net.Uri

interface CloudFileRepository {
    suspend fun uploadPlantImage(imageData: ByteArray): Uri?
}